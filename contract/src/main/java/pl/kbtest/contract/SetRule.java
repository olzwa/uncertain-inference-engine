/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.LinkedList;
import java.util.List;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;

/**
 *
 * @author Kamil
 */
public class SetRule {
    
    private final List<SetPremise> premises;
    private final List<SetAction> conclusions;
    final private GrfIrf grfIrf;
    
    public SetRule(final GrfIrf grfIrf){
        this.grfIrf = grfIrf;
        this.conclusions = new LinkedList<>();
        this.premises = new LinkedList<>();
    }
    
    public SetRule(final List<SetPremise> premises, final List<SetAction> conlusions, final GrfIrf grfIrf){
        this.premises = premises;
        this.conclusions = conlusions;
        this.grfIrf = grfIrf;
    }
    
    public void addPremises(final SetPremise premise){
        this.premises.add(premise);
    }
    
    public void addConclusion(final SetAction action){
        this.conclusions.add(action);
    }
    
    public List<SetAction> getConclusions(){
        return this.conclusions;
    }
    
    public List<SetPremise> getPremises(){
        return this.premises;
    }
    
    public GrfIrf getGrfIrf(){
        return this.grfIrf;
    }
    
}
