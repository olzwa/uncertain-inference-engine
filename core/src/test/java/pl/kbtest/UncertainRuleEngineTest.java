package pl.kbtest;

import java.util.Collections;
import org.junit.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.contract.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class UncertainRuleEngineTest {

	@Test
	public void should_execute_f4_f1_f2() {
		//given
		Context context = new Context();

		SetFact f1 = SetFactFactory.getInstance("rok", "1,2", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.8)));
		r1.addPremises(SetPremise.Factory.getInstance("kierunek informatyka", false));
		r1.addPremises(SetPremise.Factory.getInstance("rok ! 1 2", false));
		r1.addConclusion(new DefaultSetAction("sprzet", "komputer_stacjonarny,laptop", true));

		context.addFact(f1);
		context.addFact(f2);
		context.addRules(Collections.singletonList(r1));

		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		List<SetFact> actual = subject.getContext().getFacts();

		assertThat(actual).contains(SetFactFactory.getInstance("sprzet", "komputer_stacjonarny,laptop", new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.64)), true));
	}

	@Test
	public void should_execute_f3() {
		//given
		Context context = new Context();

		SetFact f1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), false);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.55)));
		r1.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk", false));
		r1.addConclusion(new DefaultSetAction("kierunek", "informatyka", true));

		context.addFact(f1);
		context.addFact(f2);
		context.addRules(Collections.singletonList(r1));

		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		List<SetFact> actual = subject.getContext().getFacts();

		SetFact expected = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.71).setScale(4)), BigDecimal.valueOf(0.36), false, true);
		assertThat(actual).containsOnly(expected, f1);
	}

	@Test
	public void should_not_execute_f3_when_conjunction_not_match() {
		//given
		Context context = new Context();

		SetFact f1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), false);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), false);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.55)));
		r1.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk", false));
		r1.addConclusion(new DefaultSetAction("kierunek", "informatyka", true));

		context.addFact(f1);
		context.addFact(f2);
		context.addRules(Collections.singletonList(r1));

		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		List<SetFact> actual = subject.getContext().getFacts();

		SetFact expected_new_fact = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.55)), BigDecimal.valueOf(1), false, true);
		assertThat(actual).containsOnly(expected_new_fact, f1, f2);
	}

	@Test
	public void should_execute_rule_with_higher_grf() {
		//given
		Context context = new Context();

		SetFact f1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), false);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.55)));
		r1.addPremises(SetPremise.Factory.getInstance("kierunek teleinformatyka", false));
		r1.addConclusion(new DefaultSetAction("kierunek", "mechatronika", true));

		SetRule r2 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.55)));
		r2.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk", false));
		r2.addConclusion(new DefaultSetAction("kierunek", "teleinformatyka", true));

		context.addFact(f1);
		context.addFact(f2);
		context.addRules(Arrays.asList(r1, r2));

		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		List<SetFact> actual = subject.getContext().getFacts();

		SetFact expected = SetFactFactory.getInstance("kierunek", "mechatronika", new GrfIrf(BigDecimal.valueOf(0.75), BigDecimal.valueOf(0.3025).setScale(4)), BigDecimal.valueOf(1), false, true);

		assertThat(actual).contains(expected);
	}

	@Test
	public void should_not_match_premise_only_on_values() {
		//given
		Context context = new Context();

		SetFact f1 = SetFactFactory.getInstance("rok", "2019,2020,2021", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
		SetFact f2 = SetFactFactory.getInstance("kierunek", "informatyka", new GrfIrf(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.8)), true);

		SetRule r1 = new SetRule(new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.8)));
		r1.addPremises(SetPremise.Factory.getInstance("rok 2019 2020", true));
		r1.addConclusion(new DefaultSetAction("sprzet", "komputer_stacjonarny,laptop", true));

		context.addFact(f1);
		context.addFact(f2);
		context.addRules(Collections.singletonList(r1));

		UncertainRuleEngine subject = new UncertainRuleEngine(context);
		//when
		subject.fireRules();
		//then
		List<SetFact> actual = subject.getContext().getFacts();

		assertThat(actual).doesNotContain(SetFactFactory.getInstance("sprzet", "komputer_stacjonarny,laptop", new GrfIrf(BigDecimal.valueOf(0.9), BigDecimal.valueOf(0.0)), true));
	}

}