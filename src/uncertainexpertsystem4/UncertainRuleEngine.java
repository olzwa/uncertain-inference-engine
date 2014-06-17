/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uncertainexpertsystem4;

import uncertainexpertsystem4.premiseEvaluator.PremiseEvaluator;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import uncertainexpertsystem4.action.Action;
import uncertainexpertsystem4.conclusionExecutor.DefaultConclusionExecutor;

/**
 *
 * @author Kamil
 */
public class UncertainRuleEngine {

    final private Context context;

    public UncertainRuleEngine(Context context) {
        this.context = context;
    }

    private Fact contextFactsContains(Fact f) {
        Fact result = null;
        for (Fact fact : context.facts) {
            if (fact.equals(f)) {
                result = fact;
            }
        }
        if (result != null) {
            System.out.println("fact already exists");
        }
        return result;
    }

    private int addFacts(List<Fact> facts, Rule rule) {
        int count = 0 ;
        for (Fact f : facts) {
            Fact search = contextFactsContains(f);
            if (search != null) {
                if (search.isAxiom()) {
                    System.out.println("found but axiom");
                } else {
                    System.out.println("found not axiom");
                    Fact propagated = PropagationFunctions.propagationF3(rule, search, f);
                    
                    count++;
                    System.out.println(propagated);
                    context.facts.add(propagated);
                    context.facts.remove(f);
                }
            } else {
                System.out.println("Fact added");
                System.out.println(f);

                context.facts.add(f);
                count++;
            }
        }
        return count;
    }

    public void fireRules() {

        DefaultConclusionExecutor dce = new DefaultConclusionExecutor();
        DefaultPremiseEvaluator dpe = new DefaultPremiseEvaluator(context.facts);

        GrfIrf premisesGrfIrf;

        for (Rule rule : context.rules) {

            boolean correctFacts = dpe.evaluate(rule);
            if (correctFacts) {
                if (rule.getPremises().size() == 1) {
                    premisesGrfIrf = rule.getPremises().get(0).getGrfIrf();
                } else {
                    premisesGrfIrf = PropagationFunctions.propagationF1(rule);
                }
                List<Fact> facts = dce.executeConclusions(rule.getConclusions(), dpe.getVariables(), rule.getGrfIrf(), premisesGrfIrf);
                if (facts.size() > 0) {
                    addFacts(facts, rule);
                }
            }

        }

    }

    private static class DefaultPremiseEvaluator {

        final private Deque<Fact> facts;
        private List<Premise> premises;
        public List<Fact> correctFacts = new LinkedList<>();
        private Map<String, String> variables;
        public List<Fact> results;

        public DefaultPremiseEvaluator(Deque<Fact> facts) {
            this.variables = new HashMap<>();
            this.facts = facts;
        }

        public void setPremises(List<Premise> premises) {
            this.premises = premises;
            this.variables = new HashMap<>();
        }

        public Map<String, String> getVariables() {
            return this.variables;
        }

        public boolean evaluate(Rule rule) {//TODO remove Rule

            boolean result = false;
            PremiseEvaluator premiseEvalutor = new PremiseEvaluator();

            List<Premise> premises = rule.getPremises();
            test:
            for (int i = 0; i < premises.size(); i++) {
                Premise premise = premises.get(i);

                List<Fact> factEquals = new LinkedList<>();
                

                System.out.println("premise: " + premise);
                for (Fact fact : facts) {
                    System.out.println("fact: " + fact);
                    premiseEvalutor.setPair(premise.getBody(), fact.getBody());
                    if (premiseEvalutor.equals()) {
                        result = true;
                        premise.setGrfIrf(fact.getGrfIrf());
                        factEquals.add(fact);
                        System.out.println("Premise correct");
                        break;
                    } else {
                        result = false;
                    }
                }
                if (result == false) {
                    System.out.println("Premise not correct");
                    break;
                }
            }

            return result;
        }
    }

    public static class PropagationFunctions {

        public static GrfIrf propagationF1(Rule r) {
            List<Premise> premises = r.getPremises();

            BigDecimal minGrf = premises.get(0).getGrfIrf().getGrf();
            BigDecimal minIrf = premises.get(0).getGrfIrf().getIrf();

            for (Premise s : premises) {
                BigDecimal grf = s.getGrfIrf().getGrf();
                BigDecimal irf = s.getGrfIrf().getIrf();

                if (grf.compareTo(minGrf) < 0) {
                    minGrf = grf;
                }
                if (irf.compareTo(irf) < 0) {
                    minIrf = irf;
                }
            }

            return new GrfIrf(minGrf, minIrf);
        }

        public static GrfIrf propagationF2(GrfIrf premiseGrfIrf, GrfIrf ruleGrfIrf) {
            BigDecimal irf = premiseGrfIrf.getIrf().multiply(ruleGrfIrf.getIrf());
            BigDecimal grf = Utils.getMin(premiseGrfIrf.getGrf(), ruleGrfIrf.getGrf());
            return new GrfIrf(grf, irf);
        }

        public static GrfIrf propagationF4(Fact f) {
            GrfIrf grfIrf = f.getGrfIrf();
            return new GrfIrf(grfIrf.getGrf(), BigDecimal.ONE.subtract(grfIrf.getIrf()));
        }

        public static Fact propagationF3(Rule r, Fact foundFact, Fact toAdd) {
            BigDecimal tParamter = new BigDecimal(1.8);

            BigDecimal pCi = foundFact.getGrfIrf().getIrf();
            BigDecimal pCj = toAdd.getGrfIrf().getIrf();

            BigDecimal denominator = tParamter.add(foundFact.getWParamter());
            BigDecimal w = foundFact.getWParamter().divide(denominator, 2, BigDecimal.ROUND_HALF_UP);//TODO

            BigDecimal part1 = (BigDecimal.ONE.subtract(w));
            part1 = part1.multiply(pCi);

            BigDecimal part2 = w.multiply(pCj);

            BigDecimal irf = part1.add(part2);
            BigDecimal grf = Utils.getMax(foundFact.getGrfIrf().getGrf(), toAdd.getGrfIrf().getGrf());

            return Fact.FactFactory.getInstance(toAdd.getBody(), new GrfIrf(grf, irf), w);
        }
    }

}
