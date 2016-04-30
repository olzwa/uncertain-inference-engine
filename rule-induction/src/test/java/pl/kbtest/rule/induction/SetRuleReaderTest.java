package pl.kbtest.rule.induction;

import com.cedarsoftware.util.DeepEquals;
import com.thoughtworks.xstream.XStream;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert.*;

/**
 * Created by Ja on 2016-04-05.
 */
public class SetRuleReaderTest {

    @Test
    public void testReadRules() throws Exception {
        Set delimiters = new HashSet<>(Arrays.asList("=", ":"));
        String conjunctionToken = new String("AND");
        File f = new File(SetRuleReader.class.getClassLoader().getResource("SetRuleReaderTestFile.txt").getFile());

        SetPremise first = new SetPremise("wiek", new HashSet<>(Arrays.asList("kolejna_wartosc", "21")), false, false);
        SetPremise second = new SetPremise("plec", new HashSet<>(Arrays.asList("kobieta")), false, false);
        SetPremise third = new SetPremise("stan_zdrowia", new HashSet<>(Arrays.asList("kolejna_wartosc2", "dobry")), false, false);
        //SetAction action = new DefaultSetAction("motywacja", "przyjemność płynąca z uprawiania sportu2 AND troska o wygląd2", true);
        SetAction action = new DefaultSetAction("motywacja", "przyjemnosc plynaca z uprawiania sportu2 AND troska o wyglad2", true);
        List<SetPremise> premises = new ArrayList();
        premises.add(first);
        premises.add(second);
        premises.add(third);
       // List conclusions = Collections.singletonList(action);
        List conclusions = Arrays.asList(action);


        SetRule rule = new SetRule(premises, conclusions, null);
        List<SetRule> expectedRules = new ArrayList<>();
        expectedRules.add(rule);

        first = new SetPremise("kolumna", new HashSet<>(Arrays.asList("wartosc2", "wartosc1")), false, true);
        second = new SetPremise("kolumna2", new HashSet<>(Arrays.asList("wartosc4", "wartosc3")), false, false);
        third = new SetPremise("kolumna3", new HashSet<>(Arrays.asList("wartosc5", "wartosc6")), false, false);
        action = new DefaultSetAction("fakt", "wartosc7", false);
        premises = new ArrayList<>();
        premises.add(first);
        premises.add(second);
        premises.add(third);

        conclusions =Arrays.asList(action); //Collections.singletonList(action);


        rule = new SetRule(premises, conclusions, null);
        expectedRules.add(rule);

        SetRuleReader srr = new SetRuleReader(f, delimiters, conjunctionToken,true);

        List<SetRule> result = srr.readRules();

        System.out.println(result.hashCode()+" <- result hashcode "+result.toString()+ "\n");
        System.out.println(expectedRules.hashCode()+" <- expected hashcode"+expectedRules.toString()+ "\n");
        boolean eq = DeepEquals.deepEquals(result,expectedRules);
        if(eq){
            System.out.println("deep equals returns true");
        }
        XStream xs = new XStream();

        String xmlResult = xs.toXML(result);
        String xmlExpected = xs.toXML(expectedRules);

       // System.out.println(xmlExpected);
       // System.out.println(xmlResult);

        Diff d = new Diff(xmlExpected,xmlResult);
        DetailedDiff dd = new DetailedDiff(d);

        List l = dd.getAllDifferences();

        for (Object t: l) {
            Difference dtemp = (Difference) t;
            System.out.println("======");
            System.out.println(dtemp);
            System.out.println("///////");
        }

        //Assert.assertEquals(result,expectedRules);
       //assertEquals(result,expectedRules);
    }
}
