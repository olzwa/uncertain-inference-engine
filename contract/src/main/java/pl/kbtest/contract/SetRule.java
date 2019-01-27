/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.MoreObjects;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.SetPremise;
import pl.poznan.put.cie.oculus.dbentries.internal.GrfIrf;

/**
 * @author Kamil
 */
public class SetRule {

    private final List<SetPremise> premises;
    private final List<SetAction> conclusions;
    final private GrfIrf grfIrf;

    public SetRule(final GrfIrf grfIrf) {
        this.grfIrf = grfIrf;
        this.conclusions = new LinkedList<>();
        this.premises = new LinkedList<>();
    }

    public SetRule(final List<SetPremise> premises, final List<SetAction> conlusions, final GrfIrf grfIrf) {
        this.premises = premises;
        this.conclusions = conlusions;
        this.grfIrf = grfIrf;
    }

    public void addPremises(final SetPremise premise) {
        this.premises.add(premise);
    }

    public void addConclusion(final SetAction action) {
        this.conclusions.add(action);
    }

    public List<SetAction> getConclusions() {
        return this.conclusions;
    }

    public List<SetPremise> getPremises() {
        return this.premises;
    }

    @Override
    public int hashCode() {
        int result = premises != null ? premises.hashCode() : 0;
        result = 31 * result + (conclusions != null ? conclusions.hashCode() : 0);
        result = 31 * result + (grfIrf != null ? grfIrf.hashCode() : 0);
        return result;
    }

    public GrfIrf getGrfIrf() {
        return this.grfIrf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof SetRule)) {
            return false;
        } else {


            for (int j = 0; j < this.getPremises().size(); j++) {
                SetPremise thisPremise = this.getPremises().get(j);
                SetPremise objPremise = ((SetRule) obj).getPremises().get(j);
                if (!thisPremise.toString().equals( objPremise.toString()) ||
                        thisPremise.getGrfIrf() != objPremise.getGrfIrf() ||
                        thisPremise.isNegated() != objPremise.isNegated() ||
                        thisPremise.isConjunction() != objPremise.isConjunction()) {
                    return false;
                }
            }
            for (int j = 0; j < this.getConclusions().size(); j++) {
                SetAction thisAction = this.getConclusions().get(j);
                SetAction objAction = ((SetRule) obj).getConclusions().get(j);
                if (!thisAction.toString().equals(objAction.toString()) || thisAction.isConjunction() != objAction.isConjunction()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("premises", premises)
                .add("conclusions", conclusions)
                .add("grfIrf", grfIrf)
                .toString();
    }
}
