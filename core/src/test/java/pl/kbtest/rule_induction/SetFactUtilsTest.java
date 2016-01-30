package pl.kbtest.rule_induction;

import org.junit.Test;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class SetFactUtilsTest {

    @Test
    public void should_match_equals_disjunction_SetFacts() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_match_equals_conjunction_SetFacts() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne,zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => caloroczne,zimowe", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_match_equals_conjunction_and_disjunction_SetFacts() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => caloroczne,zimowe", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_match_subset_when_disjunction() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => caloroczne", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_match_subset_when_disjunction2() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => zimowe", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_not_match_when_not_subset() {
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => caloroczne|zimowe", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => zimowe|inne", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isFalse();
    }

    @Test
    public void should_match_when_single_and_double_conjunction(){
        //given
        SetFact fact1 = SetFactFactory.getInstance("sporty => indywidualnie", null);
        SetFact fact2 = SetFactFactory.getInstance("sporty => indywidualnie,zespolowe", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact1, fact2);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void should_not_match_when_double_conjunction_and_single(){
        //given
        SetFact fact2 = SetFactFactory.getInstance("sporty => indywidualnie,zespolowe", null);
        SetFact fact1 = SetFactFactory.getInstance("sporty => indywidualnie", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact2, fact1);
        //then
        assertThat(actual).isFalse();
    }

    @Test
    public void should_not_match_when_different_disjunctive_and_conjunctive(){
        //given
        SetFact fact2 = SetFactFactory.getInstance("sporty => indywidualnie|zespolowe|inne", null);
        SetFact fact1 = SetFactFactory.getInstance("sporty => indywidualnie,inne", null);
        //when
        boolean actual = SetFactUtils.isSetFactSubset(fact2, fact1);
        //then
        assertThat(actual).isFalse();
    }

}