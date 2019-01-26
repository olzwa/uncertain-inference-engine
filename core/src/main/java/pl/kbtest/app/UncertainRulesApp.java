package pl.kbtest.app;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.kbtest.UncertainRuleEngine;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetRule;
import pl.kbtest.utils.Utils;
import pl.poznan.put.cie.oculus.dbentries.GrfIrf;
import pl.poznan.put.cie.oculus.dbentries.jobs.Job;
import pl.poznan.put.cie.oculus.dbentries.jobs.JobStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static pl.kbtest.app.Commands.ADD_FACT;
import static pl.kbtest.app.Commands.FIRE_RULES;
import static pl.kbtest.app.Commands.LOAD_FACTS;
import static pl.kbtest.app.Commands.LOAD_RULES;
import static pl.kbtest.app.Commands.SHOW_FACTS;
import static pl.kbtest.app.Commands.SHOW_RULES;

public class UncertainRulesApp {
    private final Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
    private Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

    private final Context context = new Context(facts, rules);
    private final UncertainRuleEngine engine = new UncertainRuleEngine(context);

    private String apiUrl;

    public UncertainRulesApp(Deque<SetRule> initialRules, Deque<SetFact> initialFacts) {
        rules.addAll(initialRules);
        facts.addAll(initialFacts);
    }

    public void cliMode() throws IOException {
        printRulesReport();
        printFactsReport();

        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            command = scanner.nextLine();
            if (command.startsWith(LOAD_RULES)) {
                String[] split = command.split(LOAD_RULES);
                File ruleFile = new File(split[1].trim());
                Deque<SetRule> loadedRules = Utils.loadJsonRulesAction(ruleFile);
                System.out.println("Loaded " + loadedRules.size() + " rules");
                rules.addAll(loadedRules);
            }
            if (command.equals(SHOW_RULES)) {
                printRules();
            }
            if (command.equals(SHOW_FACTS)) {
                printFacts();
            }
            if (command.startsWith(LOAD_FACTS)) {
                String[] split = command.split(LOAD_FACTS);
                File factsFile = new File(split[1].trim());
                Deque<SetFact> loadedFacts = Utils.loadJsonFactsAction(factsFile);
                System.out.println("Loaded " + loadedFacts.size() + " facts");
                facts.addAll(loadedFacts);
            }
            if (command.startsWith(ADD_FACT)) {
                String[] splitCommand = command.split(ADD_FACT);
                String factBody = splitCommand[1].trim();
                String[] splitFactBody = factBody.split(" ");
                Pattern grfIrfPattern = Pattern.compile("\\{([0-9]+),([0-9]+)\\}");
                Matcher m = grfIrfPattern.matcher(splitFactBody[1]);
                BigDecimal grf = null;
                BigDecimal irf = null;
                if (m.find()) {
                    grf = BigDecimal.valueOf(Integer.parseInt(m.group(1)));
                    irf = BigDecimal.valueOf(Integer.parseInt(m.group(2)));
                }
                SetFact fact = SetFactFactory.getInstance(splitFactBody[0], new GrfIrf(grf, irf));
                facts.add(fact);
                System.out.println("Added: " + fact);
            }
            if (command.equals(FIRE_RULES)) {
                engine.fireRules();
            }
        } while (!command.equals("exit"));
    }

    private static final int JOB_DISCOVERY_DELAY = 10000;

    public void serviceMode(String apiUrl) throws InterruptedException, IOException {
        this.apiUrl = apiUrl;
        getRules();
        discoverJobs();
    }

    private void discoverJobs() throws IOException, InterruptedException {
        Utils.log("looking for jobs...");
        //noinspection InfiniteLoopStatement
        while (true) {
            final Job job = getNewJob();
            if (job != null) processJob(job);
            Thread.sleep(JOB_DISCOVERY_DELAY);
        }
    }

    private void processJob(Job job) throws IOException {
        Utils.log("got job " + job.getId());

        setJobStatus(job.getId(), JobStatus.WORKING);

        facts.addAll(job.getFacts().stream()
                .map(f -> SetFactFactory.getInstance(
                        f.getHead(),
                        f.getSet().stream().map(Object::toString).collect(Collectors.joining(",")),
                        f.getGrfIrf(),
                        f.getConjunction())
                ).collect(Collectors.toCollection(ConcurrentLinkedDeque::new)));

        Deque<SetFact> conclusions = engine.fireRules();
        Utils.log("job done, sending conclusions...");

        final int code = addConclusionsToJob(job.getId(), conclusions);
        if (code != HttpURLConnection.HTTP_OK)  Utils.log("server didn't accept conclusions, job id: " + job.getId(), true);
        else setJobStatus(job.getId(), JobStatus.DONE);

        facts.clear();
    }

    private void printFacts() {
        Deque<SetFact> facts = context.getFacts();
        printFactsReport();
        facts.forEach(System.out::println);
    }

    private void printFactsReport() {
        System.out.println("Facts: " + context.getFacts().size());
    }

    private void printRules() {
        Deque<SetRule> rules = context.getRules();
        printRulesReport();
        rules.forEach(System.out::println);
    }

    private void printRulesReport() {
        System.out.println("Rules: " + context.getRules().size());
    }

    private void getRules() throws IOException {
        Utils.log("getting rules...");
        HttpURLConnection connection = makeHttpConnection("/db/rules/all", "GET");
        final int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            final String result = getRequestContent(connection);
            connection.disconnect();
            Deque<SetRule> newRules = Utils.loadJsonRulesAction(result);
            rules.addAll(newRules);
            Utils.log("received " + rules.size() + " rules");
        }
        else connection.disconnect();
    }

    private Job getNewJob() throws IOException {
        Job job = null;
        HttpURLConnection connection = makeHttpConnection("/db/jobs/new/first", "GET");
        final int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            final String result = getRequestContent(connection);
            job = Utils.loadJsonJobAction(result);
        }
        connection.disconnect();
        return job;
    }

    private int setJobStatus(String id, JobStatus status) throws IOException {
        JSONObject body = new JSONObject();
        body.put("id", id);
        body.put("status", status);
        Utils.log("setting job " +  id + " to " + status);

        return sendPut("/db/jobs/update/status", body.toString());
    }

    private int addConclusionsToJob(String id, /*List<Premise>*/Deque<SetFact> conclusions) throws IOException {
        JSONObject body = new JSONObject();
        body.put("id", id);
        JSONArray conclusionsJson = new JSONArray();
        for (final SetFact c : conclusions) {
            JSONObject conclusion = new JSONObject();
            conclusion.put("head", c.getHead());
            JSONArray set = new JSONArray(c.getSet());
            conclusion.put("set", set);
            conclusion.put("conjunction", c.isConjunction());
            conclusionsJson.put(conclusion);
        }
        body.put("conclusions", conclusionsJson);

        return sendPut("/db/jobs/update/conclusions", body.toString());
    }

    private HttpURLConnection makeHttpConnection(String path, String method) throws IOException {
        URL url = new URL(apiUrl + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        return connection;
    }

    private static String getRequestContent(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    private int sendPut(String path, String body) throws IOException {
        HttpURLConnection connection = makeHttpConnection(path, "PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        connection.disconnect();
        return connection.getResponseCode();
    }

}

/*        SetFact sf1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)),false);
        SetFact sf20 = SetFactFactory.getInstance("rok", "1 2", new GrfIrf(new BigDecimal(0.95), new BigDecimal(0.0)),true);
        SetFact sf21 = SetFactFactory.getInstance("kierunek","informatyka", new GrfIrf(new BigDecimal(0.90), new BigDecimal(0.8)),true);
        SetFact sf4 = SetFactFactory.getInstance("sprzet", "komputer_stacjonarny laptop", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)), true);

        SetRule sr1 = new SetRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        sr1.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk",false));
        sr1.addConclusion(new DefaultSetAction("kierunek informatyka", "",true));

        SetRule sr4 = new SetRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        sr4.addPremises(SetPremise.Factory.getInstance("kierunek informatyka",false));
        sr4.addPremises(SetPremise.Factory.getInstance("rok ! 1 2",false));
        sr4.addConclusion(new DefaultSetAction("sprzet komputer_stacjonarny laptop", "", false));

        facts.add(sf1);
        facts.add(sf20);
        facts.add(sf21);
        facts.add(sf4);
        rules.add(sr4);

        engine2.fireRules();
*/
