/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uncertainexpertsystem4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import static uncertainexpertsystem4.config.DefaultConfig.GLOBAL_SPLIT_REGEX;

/**
 *
 * @author Kamil
 */
public interface Fact {

    String[] getBody();

    GrfIrf getGrfIrf();

    int getLength();

    BigDecimal getWParamter();

    boolean isAxiom();

    public static class FactFactory {//Naming, should be with s at the end?

        private FactFactory() {
        }

        public static Fact getInstance(String fact, Map<String, String> wildcardFillers, GrfIrf grfIrf) {
            String[] factArr = fact.split(GLOBAL_SPLIT_REGEX);//TODO unification of this split thing
            for (int i = 0; i < factArr.length; i++) {
                if (factArr[i].startsWith("?")) {//TODO special char for variable should be configurable
                    factArr[i] = wildcardFillers.get(factArr[i]);
                }
            }
            return new DefaultFact(factArr, grfIrf);
        }

        public static Fact getInstance(String fact, GrfIrf grfIrf) {
            return new DefaultFact(fact.split(GLOBAL_SPLIT_REGEX ), grfIrf);
        }

        public static Fact getInstance(String[] fact, GrfIrf grfIrf, BigDecimal wParamter) {
            return new DefaultFact(fact, grfIrf, wParamter);
        }

        private static class DefaultFact implements Fact {

            private final GrfIrf grfIrf;
            private final String[] fact;
            private final boolean axiom;
            private final BigDecimal wParameter;

            public DefaultFact(String[] definition, GrfIrf grfIrf, boolean axiom) {//TODO axiom in rest construtors
                this.grfIrf = grfIrf;
                this.fact = definition;
                this.axiom = axiom;
                this.wParameter = new BigDecimal(BigInteger.ONE);
            }

            public DefaultFact(String[] definition, GrfIrf grfIrf) {
                this.grfIrf = grfIrf;
                this.fact = definition;
                this.axiom = false;
                this.wParameter = new BigDecimal(BigInteger.ONE);
            }

            public DefaultFact(String[] definition, GrfIrf grfIrf, BigDecimal wParamter) {
                this.grfIrf = grfIrf;
                this.fact = definition;
                this.axiom = false;
                this.wParameter = wParamter;
            }

            @Override
            public boolean equals(Object object) {
                if (object instanceof Fact) {
                    Fact f = (Fact) object;
                    return Arrays.equals(this.getBody(), f.getBody());
                }
                return false;
            }

            @Override
            public int hashCode() {
                int hash = 3;
                hash = 73 * hash + Arrays.deepHashCode(this.fact);
                return hash;
            }

            @Override
            public String[] getBody() {
                return fact;
            }

            @Override
            public int getLength() {
                return fact.length;
            }

            @Override
            public GrfIrf getGrfIrf() {
                return grfIrf;
            }

            @Override
            public boolean isAxiom() {
                return this.axiom;
            }

            @Override
            public String toString() {
                return Arrays.toString(fact) + " | Grf: " + grfIrf.getGrf() + " Irf: " + grfIrf.getIrf();
            }

            @Override
            public BigDecimal getWParamter() {
                return this.wParameter;
            }

        }

    }

}
