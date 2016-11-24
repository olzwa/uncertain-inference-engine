package pl.kbtest.rule.induction.output;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.input.GsonSetFactReader;
import pl.kbtest.rule.induction.input.GsonSetRuleReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Ja on 2016-11-23.
 */
public class GsonWriterTest {
    @Test
    public void write() throws Exception {

        //given
        File setFactFile = new File("GsonWriterSetFactTestFile.txt");
        File setRuleFile = new File("GsonWriterSetRuleTestFile.txt");

        SetFact fact = SetFactFactory.getInstance("rok = 1 AND 2",new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.valueOf(0.0)));
        List factList = Arrays.asList(fact);
        SetPremise first = new SetPremise("wydzial_rodzimy", new HashSet<>(Arrays.asList("informatyka", "elektryk")), false, true);
        SetAction action = new DefaultSetAction("kierunek=informatyka", null, true);
        List<SetPremise> premises = new ArrayList();
        premises.add(first);
        List conclusions = Arrays.asList(action);
        SetRule rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(0.55), BigDecimal.valueOf(0.75)));
        List rules = Arrays.asList(rule);

        //when
        GsonWriter gw = new GsonWriter(SetFact.class,setFactFile, false);
        gw.write(factList);
        gw = new GsonWriter(SetRule.class, setRuleFile, false);
        gw.write(rules);


        GsonSetFactReader gsfr = new GsonSetFactReader(setFactFile);
        List factsFromJson = gsfr.readFacts();

        GsonSetRuleReader gsrr = new GsonSetRuleReader(setRuleFile);
        List rulesFromJson = gsrr.readRules();
        //then

        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compareCollections(factList, factsFromJson, SetFact.class);
        System.out.println(diff);

        Assert.assertEquals(0, diff.getChanges().size());

        diff = javers.compareCollections(rules, rulesFromJson, SetRule.class);
        System.out.println(diff);

        Assert.assertEquals(0, diff.getChanges().size());

    }

}