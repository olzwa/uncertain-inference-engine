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
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
        Deque<SetFact> result = new ArrayDeque<>();
        String content = new String(Files.readAllBytes(Paths.get(factsFile.getAbsolutePath())));
        JSONArray factsArray = new JSONArray(content);
        for (Object object : factsArray) {
            JSONObject jsonFact = (JSONObject) object;
            String premiseHead = jsonFact.getString("head");
            String factBody = jsonFact.getJSONArray("set")
                    .toList()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            JSONObject jsonGrfIrf = jsonFact.getJSONObject("GrfIrf");
            BigDecimal irf = jsonGrfIrf.getBigDecimal("irf");
            BigDecimal grf = jsonGrfIrf.getBigDecimal("grf");
            GrfIrf grfIrf = new GrfIrf(grf, irf);
            boolean conjunction = jsonFact.getBoolean("conjunction");
            result.add(SetFactFactory.getInstance(premiseHead, factBody, grfIrf, conjunction));
        }
        return result;
    }

    public static Deque<SetRule> loadJsonRulesAction(File ruleFile) throws IOException {
        Deque<SetRule> result = new ArrayDeque<>();
        String content = new String(Files.readAllBytes(Paths.get(ruleFile.getAbsolutePath())));
        // Convert JSON string to JSONObject
        JSONArray rulesArray = new JSONArray(content);
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
            JSONObject jsonGrfIrf = jsonRule.getJSONObject("GrfIrf");
            BigDecimal irf = jsonGrfIrf.getBigDecimal("irf");
            BigDecimal grf = jsonGrfIrf.getBigDecimal("grf");
            GrfIrf grfIrf = new GrfIrf(grf, irf);
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
}
