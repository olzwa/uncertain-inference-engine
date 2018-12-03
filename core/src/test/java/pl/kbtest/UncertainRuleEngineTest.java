package pl.kbtest;

import org.junit.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.contract.*;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.assertj.core.api.Assertions.*;
import static org.testng.Assert.*;

public class UncertainRuleEngineTest {

	@Test
	public void should_execute_f4_f1_f2() {
		//given
		Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
		Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

		SetFact f1 = SetFactFactory.getInstance("rok", "1,2", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.8)));
		r1.addPremises(SetPremise.Factory.getInstance("kierunek informatyka", false));
		r1.addPremises(SetPremise.Factory.getInstance("rok ! 1 2", false));
		r1.addConclusion(new DefaultSetAction("sprzet", "komputer_stacjonarny,laptop", true));

		facts.add(f1);
		facts.add(f2);
		rules.add(r1);

		Context context = new Context(facts, rules);
		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		Deque<SetFact> actual = subject.getContext().getFacts();

		assertThat(actual).contains(SetFactFactory.getInstance("sprzet", "komputer_stacjonarny,laptop", new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.64)), true));
	}

	@Test
	public void should_execute_f3() {
		//given
		Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
		Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

		SetFact f1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), false);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.55)));
		r1.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk", false));
		r1.addConclusion(new DefaultSetAction("kierunek", "informatyka", true));

		facts.add(f1);
		facts.add(f2);
		rules.add(r1);

		Context context = new Context(facts, rules);
		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		Deque<SetFact> actual = subject.getContext().getFacts();

		SetFact expected = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.71).setScale(4)), BigDecimal.valueOf(0.36), false, true);
		assertThat(actual).containsOnly(expected, f1);
	}

}