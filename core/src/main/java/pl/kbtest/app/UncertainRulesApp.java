package pl.kbtest.app;

import pl.kbtest.UncertainRuleEngine;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetRule;
import pl.kbtest.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.kbtest.app.Commands.ADD_FACT;
import static pl.kbtest.app.Commands.FIRE_RULES;
import static pl.kbtest.app.Commands.LOAD_FACTS;
import static pl.kbtest.app.Commands.LOAD_RULES;
import static pl.kbtest.app.Commands.SHOW_FACTS;
import static pl.kbtest.app.Commands.SHOW_RULES;

public class UncertainRulesApp {
    private Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
    private Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

    private Context context = new Context(facts, rules);
    private UncertainRuleEngine engine = new UncertainRuleEngine(context);

    public UncertainRulesApp(Deque<SetRule> initialRules, Deque<SetFact> initialFacts) {
        rules.addAll(initialRules);
        facts.addAll(initialFacts);
    }

    public void cliMode() throws IOException {
        printRulesReport(context);
        printFactsReport(context);

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
                printRules(context);
            }
            if (command.equals(SHOW_FACTS)) {
                printFacts(context);
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

    public void serviceMode(String apiUrl) {

    }

    private static void printFacts(Context context) {
        Deque<SetFact> facts = context.getFacts();
        printFactsReport(context);
        facts.forEach(System.out::println);
    }

    private static void printFactsReport(Context context) {
        System.out.println("Facts: " + context.getFacts().size());
    }

    private static void printRules(Context context) {
        Deque<SetRule> rules = context.getRules();
        printRulesReport(context);
        rules.forEach(System.out::println);
    }

    private static void printRulesReport(Context context) {
        System.out.println("Rules: " + context.getRules().size());
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
