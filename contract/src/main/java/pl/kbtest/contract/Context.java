/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Kamil
 */
public class Context {

  private static final Comparator<SetRule> comparing = Comparator.comparing(rule -> rule.getGrfIrf().getGrf());

  private final List<SetRule> rules;
  private final List<SetFact> facts;

  public Context() {
    this.rules = new ArrayList<>();
    this.facts = new ArrayList<>();
  }

  public List<SetRule> getRules() {
    return this.rules;
  }

  public void addRules(Collection<SetRule> rules) {
    this.rules.addAll(rules);
    this.rules.sort(comparing.reversed());
  }

  public void addFacts(Collection<SetFact> facts) {
    this.facts.addAll(facts);
  }

  public void addFact(SetFact fact) {
    this.facts.add(fact);
  }

  public List<SetFact> getFacts() {
    return this.facts;
  }
}
