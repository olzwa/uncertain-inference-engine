/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.rule.induction;

import pl.kbtest.contract.SetFact;

/**
 *
 * @author Kamil
 */
public class SetUtils {
    
    public boolean containsWhenConjunctionSet(final SetFact s1,final SetFact s2){
        if(s2.getSet().size() <= s1.getSet().size()){
            if(s2.getSet().containsAll(s1.getSet())){
                return true;
            }
        }
        return false;
    }
    
}
