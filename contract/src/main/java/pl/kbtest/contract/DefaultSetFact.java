/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.math.BigDecimal;
import java.util.Set;

/**
 *
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
                    final BigDecimal wParamter, final boolean axiom,final boolean negate, final boolean conj) {
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
            public String toString(){
                return this.head+": "+this.set+" grfIrf: "+this.grfIrf;
            }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultSetFact that = (DefaultSetFact) o;

        if (head != null ? !head.equals(that.head) : that.head != null) return false;
        if (set != null ? !set.equals(that.set) : that.set != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = head != null ? head.hashCode() : 0;
        result = 31 * result + (set != null ? set.hashCode() : 0);
        return result;
    }



}