/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest;

import pl.kbtest.conclusionExecutor.SetConclusionExecutor1;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.premiseEvaluator.SetPremiseComparator;
import pl.kbtest.utils.Utils;
import pl.poznan.put.cie.oculus.dbentries.GrfIrf;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Kamil
 */
public class UncertainRuleEngine {


	final private Context context;

	public UncertainRuleEngine(Context context) {
		this.context = context;
	}

	private SetFact contextFactsContains(SetFact f) {
		SetFact result = null;
		for (SetFact fact : context.getFacts()) {
			if (SetFactUtils.compareFactBody(f, fact)) {
				result = fact;
			}
		}
		return result;
	}


	private int addFacts(List<SetFact> facts, SetRule rule) {
		int count = 0;
		for (SetFact f : facts) {
			SetFact search = contextFactsContains(f);
			if (search != null) {
				if (search.isAxiom()) {
					System.out.println("\tALREADY EXISTS IN DB AS AXIOM");
				} else {
					System.out.println("\tALREADY EXISTS IN DB");
					SetFact propagated = UncertainRuleEngine.PropagationFunctions.propagationF3(rule, search, f);
					count++;
					System.out.println("\tPROPAGATED USING F3 FUNCTION: " + propagated);
					context.getFacts().add(propagated);
					context.getFacts().remove(search);
				}
			} else {
				System.out.print("\tFACT ADDED: ");
				System.out.println(f);

				context.getFacts().add(f);
				count++;
			}
		}
		return count;
	}

	public Deque<SetFact> fireRules() {

		Deque<SetFact> added = new ConcurrentLinkedDeque<>();

		SetConclusionExecutor1 dce = new SetConclusionExecutor1();
		UncertainRuleEngine.SetPremiseEvaluator dpe = new UncertainRuleEngine.SetPremiseEvaluator(context.getFacts());

		GrfIrf premisesGrfIrf;

		for (SetRule rule : context.getRules()) {
			System.out.println("Analyzing rule: " + rule);
			boolean correctFacts = dpe.evaluate(rule);
			if (correctFacts) {
				System.out.println("Matched rule:");
				if (rule.getPremises().size() == 1) {
					premisesGrfIrf = rule.getPremises().get(0).getGrfIrf();
				} else {
					premisesGrfIrf = UncertainRuleEngine.PropagationFunctions.propagationF1(rule);
				}
				List<SetFact> facts = dce.executeConclusions(rule.getConclusions(), rule.getGrfIrf(), premisesGrfIrf);
				if (facts.size() > 0) {
					System.out.println("Added conclusion: " + facts);
					addFacts(facts, rule);
					added.addAll(facts);
				}
			}

			//System.out.println("==================================================================================");
		}

		return added;
	}

	private static class SetPremiseEvaluator {

		private Deque<SetFact> facts;
		private List<SetPremise> premises;

		public List<SetFact> correctFacts = new LinkedList<>();
		public List<SetFact> results;


		public SetPremiseEvaluator(Deque<SetFact> facts) {
			this.facts = facts;
		}

		public void setPremises(List<SetPremise> premises) {
			this.premises = premises;
		}

		public boolean evaluate(SetRule rule) {//TODO remove Rule

			boolean result = false;
			SetPremiseComparator comparator = new SetPremiseComparator();

			List<SetPremise> premises = rule.getPremises();


			for (int i = 0; i < premises.size(); i++) {
				SetPremise premise = premises.get(i);

				List<SetFact> factEquals = new LinkedList<>();


				//System.out.println("\tAnalyzing premise fact: " + premise);

				for (SetFact fact : facts) {
					//System.out.println("\tchecking fact from db: " + fact);

					if (comparator.isEquals(premise, fact)) {
						result = true;
						if (premise.isNegated()) {
							premise.setGrfIrf(PropagationFunctions.propagationF4(fact));
						} else {
							premise.setGrfIrf(fact.getGrfIrf());
						}
						factEquals.add(fact);
						//System.out.println("\tFOUND FACT TO MATCH PREMISE");
//						System.out.println();
						break;
					} else {
						result = false;

					}
				}
				if (result == false) {
					//System.out.println("\tNO FACTS TO MATCH PREMISE");
					break;
				}
			}

			return result;
		}
	}


	public static class PropagationFunctions {

		public static GrfIrf propagationF1(SetRule r) {
			List<SetPremise> premises = r.getPremises();

			BigDecimal minGrf = premises.get(0).getGrfIrf().getGrf();
			BigDecimal minIrf = premises.get(0).getGrfIrf().getIrf();

			for (SetPremise s : premises) {
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

		public static GrfIrf propagationF4(SetFact f) {
			GrfIrf grfIrf = f.getGrfIrf();
			grfIrf.getGrf().toString();
			return new GrfIrf(grfIrf.getGrf(), BigDecimal.ONE.subtract(grfIrf.getIrf()));
		}

		public static SetFact propagationF3(SetRule r, SetFact foundFact, SetFact toAdd) {
			BigDecimal tParamter = new BigDecimal(1.8);//FIXME should be configurable

			BigDecimal pCi = foundFact.getGrfIrf().getIrf();
			BigDecimal pCj = toAdd.getGrfIrf().getIrf();

			BigDecimal denominator = tParamter.add(foundFact.getWParamter());
			BigDecimal w = foundFact.getWParamter().divide(denominator, 2, BigDecimal.ROUND_HALF_UP);//TODO

			BigDecimal part1 = (BigDecimal.ONE.subtract(w));
			part1 = part1.multiply(pCi);

			BigDecimal part2 = w.multiply(pCj);

			BigDecimal irf = part1.add(part2);
			BigDecimal grf = Utils.getMax(foundFact.getGrfIrf().getGrf(), toAdd.getGrfIrf().getGrf());

			return SetFactFactory.getInstance(toAdd.getHead(), toAdd.getSet(), new GrfIrf(grf, irf), w, false, toAdd.isConjunction());
		}
	}

	public Context getContext() {
		return context;
	}
}
