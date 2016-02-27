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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultSetAction that = (DefaultSetAction) o;

        if (conjunction != that.conjunction) return false;
        if (fact != null ? !fact.equals(that.fact) : that.fact != null) return false;
        return !(output != null ? !output.equals(that.output) : that.output != null);

    }

    @Override
    public int hashCode() {
        int result = fact != null ? fact.hashCode() : 0;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        result = 31 * result + (conjunction ? 1 : 0);
        return result;
    }
}
