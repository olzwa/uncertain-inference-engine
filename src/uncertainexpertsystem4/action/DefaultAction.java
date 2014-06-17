/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uncertainexpertsystem4.action;

import java.util.Map;
import uncertainexpertsystem4.Fact;
import uncertainexpertsystem4.GrfIrf;

/**
 *
 * @author Kamil
 */
public class DefaultAction implements Action {

    final private String fact;
    final private String output;
    
    public DefaultAction(String fact, String output){
        this.fact = fact;
        this.output = output;
    }
    
    @Override
    public String getOutput(Map<String, String> wildcardFillers) {
        return output;
    }

    @Override
    public Fact execute(Map<String, String> wildcardFillers, GrfIrf grfIrf) {
        return Fact.FactFactory.getInstance(fact, wildcardFillers, grfIrf);
    }

}
