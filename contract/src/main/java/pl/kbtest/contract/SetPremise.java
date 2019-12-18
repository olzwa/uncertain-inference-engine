/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import static pl.kbtest.contract.Config.GLOBAL_SPLIT_REGEX;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kamil
 */
public class SetPremise {

    private final String head;
    private final Set<String> set;
    private final boolean negate;
    private final boolean conjunction;
    private GrfIrf grfIrf;

    public SetPremise(final String head, final Set<String> set,final boolean negate, final boolean conjunction){
        this.head = head;
        this.set = set;
        this.conjunction = conjunction;
        this.negate = negate;
    }

    public Set<String> getSet() {
        return set;
    }

    public String getHead(){
        return this.head;
    }

    public boolean isConjunction() {
        return conjunction;
    }

    public GrfIrf getGrfIrf() {
        return grfIrf;
    }

    public void setGrfIrf(final GrfIrf grfIrf){
        this.grfIrf = grfIrf;
    }

    public boolean isNegated(){
        return this.negate;
    }

    @Override
    public String toString(){
        return head+" : "+set+" | "+grfIrf;
    }

    public static class Factory{

        public static SetPremise getInstance(String premise, boolean conjunction){
            boolean negate = false;
            String[] parts = premise.split(" ");
            if(parts.length > 1){
                if(parts[1].trim().equals("!")){
                    negate = true;
                }
            }
            Set<String> body = new HashSet<>(Arrays.asList(Arrays.copyOfRange(parts,1,parts.length)));
            body.remove("!");
            return new SetPremise(parts[0],body,negate,conjunction);
        }

        public static SetPremise getInstance(final String head, final String body, boolean conj) {
            boolean negate = false;
            String[] b = body.split(GLOBAL_SPLIT_REGEX);
            if (b.length > 1) {
                if (b[1].trim().equals("!")) {
                    negate = true;
                }
            }
            Set<String> set = new HashSet<>(Arrays.asList(body.split(GLOBAL_SPLIT_REGEX)));
            return new SetPremise(head, set, negate,conj);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetPremise that = (SetPremise) o;

        if (negate != that.negate) return false;
        if (conjunction != that.conjunction) return false;
        if (head != null ? !head.equals(that.head) : that.head != null) return false;
        if (set != null ? !set.equals(that.set) : that.set != null) return false;
        return !(grfIrf != null ? !grfIrf.equals(that.grfIrf) : that.grfIrf != null);

    }

    @Override
    public int hashCode() {
        int result = head != null ? head.hashCode() : 0;
        result = 31 * result + (set != null ? set.hashCode() : 0);
        result = 31 * result + (negate ? 1 : 0);
        result = 31 * result + (conjunction ? 1 : 0);
        result = 31 * result + (grfIrf != null ? grfIrf.hashCode() : 0);
        return result;
    }
}
