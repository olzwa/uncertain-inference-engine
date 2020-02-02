/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;

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

    public GrfIrf getGrfIrf() {
        return this.grfIrf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetRule setRule = (SetRule) o;
        return Objects.equals(premises, setRule.premises) &&
            Objects.equals(conclusions, setRule.conclusions) &&
            Objects.equals(grfIrf, setRule.grfIrf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(premises, conclusions, grfIrf);
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
