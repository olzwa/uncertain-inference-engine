/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.conclusionExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pl.kbtest.Fact;
import pl.kbtest.GrfIrf;
import pl.kbtest.Rule;
import pl.kbtest.UncertainRuleEngine;
import pl.kbtest.action.Action;

/**
 *
 * @author Kamil
 */
public class DefaultConclusionExecutor {

    public List<Fact> executeConclusions(List<Action> conclusions, Map<String, String> premisesVars, GrfIrf ruleGrfIrf, GrfIrf premiseGrfIrf) {
        final List<Fact> resultFacts = new LinkedList<>();
        for (Action executor : conclusions) {
            String message = executor.getOutput(premisesVars);
            if (message != null) {
                System.out.println(message);
            }
            GrfIrf calculatedGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF2(premiseGrfIrf, ruleGrfIrf);
            Fact f = executor.execute(premisesVars, calculatedGrfIrf);
            if (f != null) {
                resultFacts.add(f);
            }
        }
        return resultFacts;
    }

    public List<Fact> executeConclusions(Rule rule, Map<String, String> premisesVars) {
        final List<Fact> resultFacts = new LinkedList<>();
        for (Action executor : rule.getConclusions()) {
            String message = executor.getOutput(premisesVars);
            if (message != null) {
                System.out.println(message);
            }
            GrfIrf premiseGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF1(rule);
            GrfIrf calculatedGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF2(premiseGrfIrf, rule.getGrfIrf());
            Fact f = executor.execute(premisesVars, calculatedGrfIrf);
            if (f != null) {
                resultFacts.add(f);
            }
        }
        return resultFacts;
    }

}
