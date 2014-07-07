/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import pl.kbtest.action.Action;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kamil
 */
public class Rule {

    final private GrfIrf grfIrf;
    final private List<Premise> premises;
    final private List<Action> conclusions;

    public Rule(GrfIrf grfIrf) {
        this.grfIrf = grfIrf;
        this.premises = new LinkedList<>();
        this.conclusions = new LinkedList<>();
    }

    public Rule(GrfIrf grfIrf, List<Premise> premises, List<Action> conclusions) {
        this.grfIrf = grfIrf;
        this.premises = premises;
        this.conclusions = conclusions;
    }

    @Override
    public String toString() {
        return premises.toString() + " => " + conclusions.toString();
    }

    public void addPremise(Premise f) {
        premises.add(f);
    }

    public List<Premise> getPremises() {
        return premises;
    }

    public void addConclusion(Action a) {
        conclusions.add(a);
    }

    public List<Action> getConclusions() {
        return conclusions;
    }

    public GrfIrf getGrfIrf() {
        return grfIrf;
    }

}
