package pl.kbtest;


import org.junit.Assert;
import pl.kbtest.rule.induction.input.CSVParser;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class CSVParserTest {

    @org.junit.Test
    public void testParse() throws Exception {

        CSVParser parser = new CSVParser(Arrays.asList("_","&"));
        parser.setSeparator("\t");


        try {
            File file = new File(CSVParser.class.getClassLoader().getResource("ankietatestowa.tsv").getFile());

            parser.parse(file);

            List<List<String>> correctRows = new LinkedList<>();

            String[] cells;

            cells = new String[]{"Rok-miesiac-dzien","111","aaa, bbb","ccc"};
            correctRows.add(Arrays.asList(cells));
            cells = new String[]{"Rok-miesiac-dzien","444","aaa4","bbb4"};
            correctRows.add(Arrays.asList(cells));
            cells = new String[]{"Rok-miesiac-dzien","555","aaa5, aa, a","bbb5"};
            correctRows.add(Arrays.asList(cells));

            Assert.assertEquals(correctRows,parser.getRows());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}