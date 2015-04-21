/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.conclusionExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;
import pl.kbtest.UncertainRuleEngine;
import pl.kbtest.action.SetAction;


/**
 *
 * @author Kamil
 */
public class SetConclusionExecutor1 {

    public List<SetFact> executeConclusions(List<SetAction> conclusions, GrfIrf ruleGrfIrf, GrfIrf premiseGrfIrf) {
        final List<SetFact> resultFacts = new LinkedList<>();
        for (SetAction executor : conclusions) {
//            String message = executor.getOutput(premisesVars);
//            if (message != null) {
//                System.out.println(message);
//            }
            GrfIrf calculatedGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF2(premiseGrfIrf, ruleGrfIrf);
            //SimpleFact f = executor.execute(premisesVars, calculatedGrfIrf);
            SetFact f = executor.execute(null, calculatedGrfIrf, executor.isConjunction());
            if (f != null) {
                resultFacts.add(f);
            }
        }
        return resultFacts;
    }

    public List<SetFact> executeConclusions(SetRule rule) {
        final List<SetFact> resultFacts = new LinkedList<>();
        for (SetAction executor : rule.getConclusions()) {
            //String message = executor.getOutput(premisesVars);
            //if (message != null) {
            //    System.out.println(message);
            // }
            GrfIrf premiseGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF1(rule);
           // GrfIrf calculatedGrfIrf = SimpleUncertainRuleEngine.PropagationFunctions.propagationF2(premiseGrfIrf, rule.getGrfIrf());
            
            //executor.execute(null, premiseGrfIrf)
            
            //SetFact f = executor.execute(calculatedGrfIrf);
//            if (f != null) {
//                resultFacts.add(f);
//            }
        }
        return resultFacts;
    }

}
