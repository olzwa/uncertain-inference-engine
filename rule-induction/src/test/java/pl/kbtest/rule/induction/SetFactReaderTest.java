package pl.kbtest.rule.induction;

import org.junit.Assert;
import org.junit.Test;
import pl.kbtest.contract.DefaultSetFact;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Ja on 2016-04-22.
 */
public class SetFactReaderTest {

    @Test
    public void testParse() throws Exception {
        File f = new File(SetRuleReader.class.getClassLoader().getResource("SetFactReaderTestFile.txt").getFile());
        String conjunctionToken = "AND";
        String disjunctionToken = "OR";
        Set<String> delimiters = new HashSet<String>(Arrays.asList(","));
        SetFactReader sfr = new SetFactReader(f,delimiters,conjunctionToken,disjunctionToken);

        List<SetFact> facts =sfr.readFacts();

        String expectedHead = "head1";
        String expectedBody = "body";
        boolean negate = false;
        boolean conjunction = false;

        SetFact expectedFact = SetFactFactory.getInstance(expectedHead,expectedBody,null,null,false,conjunction);

        List<SetFact> expectedFacts = Arrays.asList(expectedFact);

        expectedHead ="head2";
        Set<String> expectedBodySet = new HashSet<>(Arrays.asList("b1","b2"));
        expectedFact = new DefaultSetFact(expectedHead,expectedBodySet,null,null,false,false,false);
        expectedFacts.add(expectedFact);

        expectedHead ="head3";
        expectedBodySet = new HashSet<>(Arrays.asList("b1","b2"));
        expectedFact = new DefaultSetFact(expectedHead,expectedBodySet,null,null,false,false,true);
        expectedFacts.add(expectedFact);

        Assert.assertEquals(facts,expectedFacts);

    }
}