package pl.kbtest.rule.induction.input;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Assert;
import org.testng.annotations.Test;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ja on 2016-11-12.
 */
public class GsonSetFactReaderTest {
    @Test
    public void testReadFacts() throws Exception {
        // given
        SetFact fact = SetFactFactory.getInstance("rok = 1 AND 2",new GrfIrf(BigDecimal.valueOf(0.95), BigDecimal.valueOf(0.0)));
        List<SetFact> facts = Arrays.asList(fact);
        System.out.println(fact);
        // when
        File f = new File(GsonSetFactReaderTest.class.getClassLoader().getResource("GsonSetFactTestFile.txt").getFile());
        List<SetFact> factsFromFile = new GsonSetFactReader(f).readFacts();
        // then
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compareCollections(facts, factsFromFile, SetFact.class);
        System.out.println(diff);

        Assert.assertEquals(0, diff.getChanges().size());
    }

}