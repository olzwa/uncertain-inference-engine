package pl.kbtest;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;

import java.math.BigDecimal;


public class SetFactUtilsTest {

  //TODO case when heads are not equals?
  //TODO case when comparing dis with conj?


  @Test
  public void should_not_match_when_first_is_subset_of_second_disjunction_SetFacts() {
    //given

    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe|inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isFalse();
  }

  @Test
  public void should_match_when_first_is_subset_of_second_disjunction_SetFacts() {
    //given

    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe|inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_when_subset_disjunction_SetFacts() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe,inne",  false);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe|inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_equals_disjunction_SetFacts() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);


    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne|zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_equals_conjunction_SetFacts() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  true);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

//    SetFact fact1 = SetFact.create("sporty => caloroczne,zimowe");
//    SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_equals_disjunction_with_conjunction_SetFacts() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_equals_conjunction_with_disjunction_SetFacts() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");

    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_not_match_equals_conjunction_with_disjunction_SetFacts2() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  true);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne,zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isFalse();
  }

  @Test
  public void should_match_subset_when_disjunction_element1() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_subset_when_disjunction_element1_1() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "caloroczne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => caloroczne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }


  @Test
  public void should_match_subset_when_disjunction_element2() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_subset_when_disjunction_element2_2() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_not_match_subset_single_element_matches_disjunctive() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "zimowe",  false);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => zimowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isFalse();
  }

  @Test
  public void should_not_match_when_not_subset() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,zimowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "zimowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);

    //SetFact fact1 = SetFact.create("sporty => caloroczne|zimowe");
    //SetFact fact2 = SetFact.create("sporty => zimowe|inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isFalse();
  }


  @Test
  public void should_match_when_single_and_double_conjunction() {
    //given
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "indywidualnie",  true);
    SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);
    //SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact1 = SetFact.create("sporty => indywidualnie");
    //SetFact fact2 = SetFact.create("sporty => indywidualnie,zespolowe");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact2);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_not_match_when_double_conjunction_and_single() {
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "indywidualnie,zespolowe",  true);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);
    //SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //given
    //SetFact fact2 = SetFact.create("sporty => indywidualnie,zespolowe");
    //SetFact fact1 = SetFact.create("sporty => indywidualnie");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isFalse();
  }

  @Test
  public void should_match_when_different_disjunctive_and_conjunctive() {
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "indywidualnie,zespolowe,inne",  false);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //given
    //SetFact fact2 = SetFact.create("sporty => indywidualnie|zespolowe|inne");
    //SetFact fact1 = SetFact.create("sporty => indywidualnie,inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_match_when_different_disjunctive_and_conjunctive2() {
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "caloroczne,zimowe",  false);
    SetFact fact1 = SetFactFactory.getInstance("sporty", "caloroczne,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //given
    //SetFact fact2 = SetFact.create("sporty => indywidualnie|zespolowe|inne");
    //SetFact fact1 = SetFact.create("sporty => indywidualnie,inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isTrue();
  }

  @Test
  public void should_no_match_when_non_equal_heads() {
    SetPremise premise1 = SetPremise.Factory.getInstance("sporty", "indywidualnie,zespolowe,inne",  false);
    SetFact fact1 = SetFactFactory.getInstance("inne", "indywidualnie", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //SetFact fact2 = SetFactFactory.getInstance("sporty", "indywidualnie,zespolowe,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), false);
    //SetFact fact1 = SetFactFactory.getInstance("sporty", "indywidualnie,inne", new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.ZERO), true);

    //given
    //SetFact fact2 = SetFact.create("sporty => indywidualnie|zespolowe|inne");
    //SetFact fact1 = SetFact.create("sporty => indywidualnie,inne");
    //when
    boolean actual = SetFactUtils.isMatch(premise1, fact1);
    //then
    assertThat(actual).isFalse();
  }

}