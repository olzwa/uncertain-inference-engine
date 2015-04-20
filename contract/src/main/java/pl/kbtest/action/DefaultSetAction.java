/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.action;

import java.util.Map;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

/**
 *
 * @author Kamil
 */
public class DefaultSetAction implements SetAction {

    final private String fact;
    final private String output;
    final boolean conjunction;
    
    public DefaultSetAction(String fact, String output, final boolean conj){
        this.fact = fact;
        this.output = output;
        this.conjunction = conj;
    }
    
    @Override
    public String getOutput(Map<String, String> wildcardFillers) {
        return output;
    }

    @Override
    public SetFact execute(Map<String, String> wildcardFillers, GrfIrf grfIrf, boolean conj) {
        return SetFactFactory.getInstance(fact, grfIrf, conj);
    }

    @Override
    public boolean isConjunction() {
        return conjunction;
    }
    
    

}
