package pl.kbtest.rule.induction.input;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.testng.annotations.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tomasz on 11.11.16.
 */
public class GsonSetRuleReaderTest {
    @Test
    public void testReadRules() throws Exception {
        // given
        List<SetRule> rules = new ArrayList<>();

        SetPremise first = new SetPremise("wydzial_rodzimy", new HashSet<>(Arrays.asList("informatyka", "elektryk")), false, true);
        SetAction action = new DefaultSetAction("kierunek=informatyka", null, true);
        List<SetPremise> premises = new ArrayList();
        premises.add(first);
        List conclusions = Arrays.asList(action);

        SetRule rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(0.55), BigDecimal.valueOf(0.75)));
        rules.add(rule);
        // when
        File f = new File(GsonSetRuleReaderTest.class.getClassLoader().getResource("GsonSetRuleTestFile.txt").getFile());
        List<SetRule> rulesFromFile = new GsonSetRuleReader(f).readRules();
        // then
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compareCollections(rules, rulesFromFile, SetRule.class);
        System.out.println(diff);

        Assert.assertEquals(0, diff.getChanges().size());

    }

}