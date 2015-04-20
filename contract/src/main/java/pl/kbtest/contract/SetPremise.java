/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

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
        
    }
}
