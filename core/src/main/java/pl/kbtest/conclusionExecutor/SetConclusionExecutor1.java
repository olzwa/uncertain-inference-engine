/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.conclusionExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;
import pl.kbtest.UncertainRuleEngine;
import pl.kbtest.action.SetAction;
import pl.poznan.put.cie.oculus.dbentries.GrfIrf;


/**
 *
 * @author Kamil
 */
public class SetConclusionExecutor1 {

    public List<SetFact> executeConclusions(List<SetAction> conclusions, GrfIrf ruleGrfIrf, GrfIrf premiseGrfIrf) {
        final List<SetFact> resultFacts = new LinkedList<>();
        for (SetAction executor : conclusions) {
            GrfIrf calculatedGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF2(premiseGrfIrf, ruleGrfIrf);
            SetFact f = executor.execute(null, calculatedGrfIrf, executor.isConjunction());
            if (f != null) {
                resultFacts.add(f);
            }
        }
        return resultFacts;
    }

}
