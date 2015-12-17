package pl.kbtest;

import pl.kbtest.core.ReadCVS;


import org.junit.Assert;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class ReadCVSTest {

    @org.junit.Before
    public void setUp() throws Exception {

        ReadCVS parser = new ReadCVS();
        parser.setSeparator("\t");

    }

    @org.junit.Test
    public void testParse() throws Exception {

        ReadCVS parser = new ReadCVS();
        parser.setSeparator("\t");
        parser.addCondition("_");
        parser.parse("ankietatestowa.tsv");

        List<List<String>> correctRows = new LinkedList<>();

        String[] cells;

        cells = new String[]{"Rok-miesiac-dzien","111","aaa, bbb","ccc"};
        correctRows.add(Arrays.asList(cells));
        cells = new String[]{"Rok-miesiac-dzien","444","aaa4","bbb4"};
        correctRows.add(Arrays.asList(cells));
        cells = new String[]{"Rok-miesiac-dzien","555","aaa5, aa, a","bbb5"};
        correctRows.add(Arrays.asList(cells));

        Assert.assertEquals(correctRows,parser.getRows());

    }
    
}