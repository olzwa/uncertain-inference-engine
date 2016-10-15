package pl.kbtest.rule.induction;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.contract.Config;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ja on 2016-04-22.
 */
public class SetFactReaderTest {

    @Test
    public void testParse() throws Exception {
        File f = new File(SetFactReader.class.getClassLoader().getResource("SetFactReaderTestFile.txt").getFile());
        String conjunctionToken = Config.FACT_CONJUNCTION /*"AND"*/;
        String disjunctionToken = Config.FACT_DISJUNCTION /*"OR"*/;
        Set<String> delimiters = new HashSet<>(Arrays.asList(Config.GLOBAL_SPLIT_REGEX /*","*/));
        SetFactReader sfr = new SetFactReader(f, delimiters, conjunctionToken, disjunctionToken);

        List<SetFact> facts = sfr.readFacts();

        String expectedHead = "samplehead1";
        String expectedBody = "body";
        boolean negate = false;


        SetFact expectedFact = SetFactFactory.getInstance(expectedHead, expectedBody, null, null, false, true);

        List<SetFact> expectedFacts = new ArrayList<>();
        expectedFacts.add(expectedFact);

        expectedHead = "samplehead2";
        Set<String> expectedBodySet = new HashSet<>(Arrays.asList("b1", "b2"));
        expectedFact = SetFactFactory.getInstance(expectedHead, expectedBodySet, null, null, true, false);
        expectedFacts.add(expectedFact);

        expectedHead = "samplehead3";
        expectedBodySet = new HashSet<>(Arrays.asList("b1", "b2"));
        expectedFact = SetFactFactory.getInstance(expectedHead, expectedBodySet, null, null, false, true);
        expectedFacts.add(expectedFact);
/*


        expectedHead =
                "16. Scharakteryzuj stan swojego zdrowia.=dobry 8. Scharakteryzuj najczęściej używaną przez Ciebie odzież sportową. =markowa AND średniej klasy 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta 2. Podaj swój wiek (w latach):=21";
        expectedBodySet = new HashSet<>(Arrays.asList("9. Na co przeznaczasz zasadniczą część swoich wakacji (urlopu)? =spotkania towarzyskie","sen i lektura"));
        expectedFact = SetFactFactory.getInstance(expectedHead,expectedBodySet,null,null,false,true);
        expectedFacts.add(expectedFact);

        expectedHead =
                "13. Oceń swoje dotychczasowe postępy w nauce i pracy. =bardzo dobre 16. Scharakteryzuj stan swojego zdrowia.=dobry 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta 2. Podaj swój wiek (w latach):=21";
        expectedBody ="11. Czy palisz papierosy? Zaznacz odpowiedź. =sporadycznie";
        expectedFact = SetFactFactory.getInstance(expectedHead,expectedBody,null,null,false,true);
        expectedFacts.add(expectedFact);

        expectedHead =
                "13. Oceń swoje dotychczasowe postępy w nauce i pracy. =bardzo dobre 16. Scharakteryzuj stan swojego zdrowia.=dobry 1. Podaj swoją płeć. Zaznacz odpowiedź. =kobieta 2. Podaj swój wiek (w latach):=21";
        expectedBodySet = new HashSet<>(Arrays.asList("3. Do jakiej kategorii (I) należą uprawiane przez Ciebie sporty?=sporty całoroczne","sporty zimowe"));
        expectedFact = SetFactFactory.getInstance(expectedHead,expectedBodySet,null,null,false,false);
        expectedFacts.add(expectedFact);

*/
        // Collection disjunction = org.apache.commons.collections4.CollectionUtils.disjunction(expectedFacts,facts);
        // System.out.println(disjunction);
        assertEquals(expectedFacts, facts);

        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compareCollections(expectedFacts, facts, SetFact.class);
        if (diff.getChanges().size() != 0) {
            String expected = expectedFacts.stream().map(SetFact::toString).reduce("", (a, b) -> a + b);
            System.out.println(expected);
            String actual = facts.stream().map(SetFact::toString).reduce("", (a, b) -> a + b);
            System.out.println(actual);
        }

        Assert.assertEquals(0, diff.getChanges().size());


    }
}