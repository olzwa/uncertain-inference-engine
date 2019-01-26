/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.poznan.put.cie.oculus.dbentries.Fact;
import pl.poznan.put.cie.oculus.dbentries.GrfIrf;
import pl.poznan.put.cie.oculus.dbentries.Premise;
import pl.poznan.put.cie.oculus.dbentries.jobs.Job;
import pl.poznan.put.cie.oculus.dbentries.jobs.JobStatus;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Kamil
 */
public class Utils {
    private Utils(){
        
    }

    public static BigDecimal getMax(BigDecimal one, BigDecimal two){
        return (one.compareTo(two) < 0) ? two   : one;
    }

    public static BigDecimal getMin(BigDecimal one, BigDecimal two){
        return (one.compareTo(two) < 0) ? one   : two;
    }

    public static Deque<SetFact> loadJsonFactsAction(File factsFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(factsFile.getAbsolutePath())));
        return loadJsonFactsAction(content);
    }

    public static Deque<SetFact> loadJsonFactsAction(String factsString) {
        Deque<SetFact> result = new ArrayDeque<>();
        JSONArray factsArray = new JSONArray(factsString);
        for (Object object : factsArray) {
            JSONObject jsonFact = (JSONObject) object;
            Fact fact = loadFactEntryAction(jsonFact);
            String factBody = fact.getSet()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            result.add(SetFactFactory.getInstance(fact.getHead(), factBody, fact.getGrfIrf(), fact.getConjunction()));
        }
        return result;
    }

    public static Deque<SetRule> loadJsonRulesAction(File ruleFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(ruleFile.getAbsolutePath())));
        return loadJsonRulesAction(content);
    }

    public static Deque<SetRule> loadJsonRulesAction(String ruleString) {
        Deque<SetRule> result = new ArrayDeque<>();
        JSONArray rulesArray = new JSONArray(ruleString);
        for (Object object : rulesArray) {
            JSONObject jsonRule = (JSONObject) object;
            //premise
            List<SetPremise> premisesList = new ArrayList<>();
            JSONArray premises = jsonRule.getJSONArray("premises");
            for (Object o : premises) {
                JSONObject jsonPremise = (JSONObject) o;
                String premiseHead = jsonPremise.getString("head");
                Set<String> premiseSet = jsonPremise.getJSONArray("set")
                        .toList()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet());
                boolean conjunction = jsonPremise.getBoolean("conjunction");
                SetPremise setPremise = new SetPremise(premiseHead, premiseSet, false, conjunction);
                premisesList.add(setPremise);
            }
            //grfIrf
            GrfIrf grfIrf = getGrfIrf(jsonRule);
            //conclusion
            JSONObject jsonConclusion = jsonRule.getJSONObject("conclusion");
            String conclusionHead = jsonConclusion.getString("head");
            String conclusion = jsonConclusion.getJSONArray("set")
                    .toList()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            boolean isConjunction = jsonConclusion.getBoolean("conjunction");
            SetAction setAction = new DefaultSetAction(conclusionHead, conclusion, isConjunction);
            result.add(new SetRule(premisesList, Collections.singletonList(setAction), grfIrf));
        }
        return result;
    }

    private static GrfIrf getGrfIrf(JSONObject json) {
        if (json.isNull("grfIrf")) return null;
        JSONObject jsonGrfIrf = json.getJSONObject("grfIrf");
        BigDecimal irf = jsonGrfIrf.getBigDecimal("irf");
        BigDecimal grf = jsonGrfIrf.getBigDecimal("grf");
        return new GrfIrf(grf, irf);
    }

    public static Job loadJsonJobAction(String jobString) {
        JSONObject jsonJob = new JSONObject(jobString);
        String id = jsonJob.getString("id");
        JobStatus status = JobStatus.valueOf(jsonJob.getString("status"));
        List<Fact> facts = loadJsonFactsEntriesAction(jsonJob.getJSONArray("facts"));
        List<Premise> conclusions = new ArrayList<>();
        return new Job(id, status, facts, conclusions);
    }

    private static List<Fact> loadJsonFactsEntriesAction(JSONArray factsJson) {
        List<Fact> result = new ArrayList<>();
        for (Object object : factsJson) {
            JSONObject jsonFact = (JSONObject) object;
            Fact fact = loadFactEntryAction(jsonFact);
            result.add(fact);
        }
        return result;
    }

    private static Fact loadFactEntryAction(JSONObject jsonFact) {
        String premiseHead = jsonFact.getString("head");
        String factBody = jsonFact.getJSONArray("set")
                .toList()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        GrfIrf grfIrf = getGrfIrf(jsonFact);
        boolean conjunction = jsonFact.getBoolean("conjunction");
        assert grfIrf != null;
        return new Fact(premiseHead, Arrays.asList(factBody.split(" ")), conjunction, grfIrf);
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS");

    public static void log(String msg) {
        log(msg, false);
    }

    public static void log(String msg, boolean error) {
        String time = formatter.format(LocalDateTime.now());
        System.out.println(time + " " + (error ? "ERROR: " : "") + msg);
    }
}
