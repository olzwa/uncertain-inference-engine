/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Kamil
 */
public class Context {

  private final SortedSet<SetRule> rules;
  private final List<SetFact> facts;

  public Context() {
    Comparator<SetRule> comparing = Comparator.comparing(rule -> rule.getGrfIrf().getGrf());
    this.rules = new TreeSet<>(comparing.reversed());
    this.facts = new ArrayList<>();
  }

  public SortedSet<SetRule> getRules() {
    return this.rules;
  }

  public void addRules(Collection<SetRule> rules) {
    this.rules.addAll(rules);
  }

  public void addRule(SetRule rule) {
    this.rules.add(rule);
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
