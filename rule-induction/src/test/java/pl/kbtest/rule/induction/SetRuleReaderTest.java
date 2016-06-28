package pl.kbtest.rule.induction;

import com.cedarsoftware.util.DeepEquals;
import com.thoughtworks.xstream.XStream;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert.*;
import org.javers.*;

/**
 * Created by Ja on 2016-04-05.
 */
public class SetRuleReaderTest {

    @Test
    public void testReadRules() throws Exception {

        Set delimiters = new HashSet<>(Arrays.asList("="));
        String conjunctionToken = "AND";
        String disjunctionToken = "OR";
        File f = new File(SetRuleReader.class.getClassLoader().getResource("SetRuleReaderTestFile.txt").getFile());

        // [1,2] 16. Scharakteryzuj stan swojego zdrowia.=dobry 8. Scharakteryzuj najczęściej używaną przez Ciebie odzież sportową. =markowa
        // AND średniej klasy 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta 2. Podaj swój wiek (w latach):=21
        // => 9. Na co przeznaczasz zasadniczą część swoich wakacji (urlopu)? =spotkania towarzyskie AND sen i lektura

        SetPremise first = new SetPremise("16. Scharakteryzuj stan swojego zdrowia.", new HashSet<>(Arrays.asList("dobry")), false, true);
        SetPremise second = new SetPremise("8. Scharakteryzuj najczęściej używaną przez Ciebie odzież sportową.", new HashSet<>(Arrays.asList("markowa","średniej klasy")), false, true);
        SetPremise third = new SetPremise("1. Podaj swoją płeć. Zaznacz odpowiedź.", new HashSet<>(Arrays.asList("kobieta")), false, true);
        SetPremise fourth = new SetPremise("2. Podaj swój wiek (w latach):", new HashSet<>(Arrays.asList("21")), false, true);
        SetAction action = new DefaultSetAction("9. Na co przeznaczasz zasadniczą część swoich wakacji (urlopu)?", "spotkania towarzyskie AND sen i lektura", true);
        List<SetPremise> premises = new ArrayList();
        premises.add(first);
        premises.add(second);
        premises.add(third);
        premises.add(fourth);
        List conclusions = Arrays.asList(action);
        SetRule rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(1),BigDecimal.valueOf(2)));
        List<SetRule> expectedRules = new ArrayList<>();
        expectedRules.add(rule);

        //[3,4] 13. Oceń swoje dotychczasowe postępy w nauce i pracy. =bardzo dobre
        // 16. Scharakteryzuj stan swojego zdrowia.=dobry 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta
        // 2. Podaj swój wiek (w latach):=21  => 11. Czy palisz papierosy? Zaznacz odpowiedź. =sporadycznie

        first = new SetPremise("13. Oceń swoje dotychczasowe postępy w nauce i pracy.", new HashSet<>(Arrays.asList("bardzo dobre")), false, true);
        second = new SetPremise("16. Scharakteryzuj stan swojego zdrowia.", new HashSet<>(Arrays.asList("dobry")), false, true);
        third = new SetPremise("1. Podaj swoją płeć. Zaznacz odpowiedź.", new HashSet<>(Arrays.asList("kobieta")), false, true);
        fourth = new SetPremise("2. Podaj swój wiek (w latach):", new HashSet<>(Arrays.asList("21")), false, true);
        action = new DefaultSetAction("11. Czy palisz papierosy? Zaznacz odpowiedź.", "sporadycznie", true);
        premises = new ArrayList<>();
        premises.add(first);
        premises.add(second);
        premises.add(third);
        premises.add(fourth);
        conclusions =Arrays.asList(action);
        rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(3),BigDecimal.valueOf(4)));
        expectedRules.add(rule);

        //[10,10]13. Oceń swoje dotychczasowe postępy w nauce i pracy. =bardzo dobre
        // 16. Scharakteryzuj stan swojego zdrowia.=dobry 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta
        // 2. Podaj swój wiek (w latach):=21  => 3. Do jakiej kategorii (I) należą uprawiane przez Ciebie sporty?=sporty całoroczne OR sporty zimowe

        first = new SetPremise("13. Oceń swoje dotychczasowe postępy w nauce i pracy.", new HashSet<>(Arrays.asList("bardzo dobre")), false, true);
        second = new SetPremise("16. Scharakteryzuj stan swojego zdrowia.", new HashSet<>(Arrays.asList("dobry")), false, true);
        third = new SetPremise("1. Podaj swoją płeć. Zaznacz odpowiedź.", new HashSet<>(Arrays.asList("kobieta")), false, true);
        fourth = new SetPremise("2. Podaj swój wiek (w latach):", new HashSet<>(Arrays.asList("21")), false, true);
        action = new DefaultSetAction("3. Do jakiej kategorii (I) należą uprawiane przez Ciebie sporty?", "sporty całoroczne OR sporty zimowe", false);
        premises = new ArrayList<>();
        premises.add(first);
        premises.add(second);
        premises.add(third);
        premises.add(fourth);
        conclusions =Arrays.asList(action);
        rule = new SetRule(premises, conclusions, new GrfIrf(BigDecimal.valueOf(10),BigDecimal.valueOf(10)));
        expectedRules.add(rule);


//
        SetRuleReader srr = new SetRuleReader(f, delimiters, conjunctionToken, disjunctionToken,false);

        List<SetRule> result = srr.readRules();

        /*System.out.println(result.hashCode()+" <- result hashcode "+result.toString()+ "\n");
        System.out.println(expectedRules.hashCode()+" <- expected hashcode"+expectedRules.toString()+ "\n");
        boolean eq = DeepEquals.deepEquals(result,expectedRules);
        if(eq){
            System.out.println("deep equals returns true");
        }*/
      /*  XStream xs = new XStream();

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
*/
        //javers
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compareCollections(expectedRules,result,SetRule.class);
        System.out.println("Javers diff: \n"+diff);
        //

        /*for (int i = 0; i < result.size() ; i++) {
            System.out.println("compared rules: "+i);
            Assert.assertEquals(result.get(i),expectedRules.get(i));
        }*/
        Assert.assertEquals(0,diff.getChanges().size());
        //Assert.assertEquals(result,expectedRules);
       //assertEquals(result,expectedRules);


    }
}
