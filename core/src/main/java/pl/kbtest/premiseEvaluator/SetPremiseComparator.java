/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.premiseEvaluator;

import java.math.BigDecimal;
import java.util.Set;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetPremise;


/**
 *
 * @author Kamil
 */
public class SetPremiseComparator {

    public boolean isEquals(final SetPremise premise, final SetFact fact) {
        if (premise.getHead().equals(fact.getHead())) {
            //if (!premise.isConjunction()) {
                for(String elem: premise.getSet()){
                    if(fact.getSet().contains(elem)){
                        return true;
                    }
                }
                
            //}
        }
        return false;
    }

}
