/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * @author Kamil
 */
public class DefaultSetFact implements SetFact {

    private final String head;
    private final Set<String> set;
    private final GrfIrf grfIrf;

    private final BigDecimal wParamter;
    private final boolean axiom;
    private final boolean conjunction;
    private final boolean negate;

    public DefaultSetFact(final String head, final Set<String> set, final GrfIrf grfIrf,
                          final BigDecimal wParamter, final boolean axiom, final boolean negate, final boolean conj) {
        this.head = head;
        this.set = set;
        this.grfIrf = grfIrf;
        this.wParamter = wParamter;
        this.axiom = axiom;
        this.negate = negate;
        this.conjunction = conj;

    }

    @Override
    public String getHead() {
        return head;
    }

    @Override
    public Set<String> getSet() {
        return set;
    }

    @Override
    public GrfIrf getGrfIrf() {
        return grfIrf;
    }

    @Override
    public int getLength() {
        return set.size();
    }

    @Override
    public BigDecimal getWParamter() {
        return wParamter;
    }

    @Override
    public boolean isAxiom() {
        return axiom;
    }

    @Override
    public boolean isConjunction() {
        return conjunction;
    }

    @Override
    public boolean isNegated() {
        return this.negate;
    }

    @Override
    public String toString() {
        return "DefaultSetFact{" +
                "head='" + head + '\'' +
                ", set=" + set +
                ", grfIrf=" + grfIrf +
                ", wParamter=" + wParamter +
                ", axiom=" + axiom +
                ", conjunction=" + conjunction +
                ", negate=" + negate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSetFact that = (DefaultSetFact) o;
        return axiom == that.axiom &&
                conjunction == that.conjunction &&
                negate == that.negate &&
                Objects.equals(head, that.head) &&
                Objects.equals(set, that.set) &&
                Objects.equals(grfIrf, that.grfIrf) &&
                Objects.equals(wParamter, that.wParamter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, set, grfIrf, wParamter, axiom, conjunction, negate);
    }
}