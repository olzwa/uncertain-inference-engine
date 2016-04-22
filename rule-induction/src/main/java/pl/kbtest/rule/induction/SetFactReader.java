package pl.kbtest.rule.induction;

import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ja on 2016-04-20.
 */

public class SetFactReader {

    File file = null;
    Set<String> delimitersSet = null;
    String columnDelimiter = null;
    String conjunctionToken = null;
    String disjunctionToken = null;
    //String valueSeparator = ",";

    public SetFactReader(File file, Set<String> delimitersSet, String conjunctionToken, String disjunctionToken) {
        this.file = file;
        this.delimitersSet = delimitersSet;
        this.conjunctionToken = conjunctionToken;
        this.disjunctionToken = disjunctionToken;
    }


    public List<SetFact> readFacts() {

        List<SetFact> facts = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));



            String currentLine = bf.readLine();
            while (currentLine != null) {
                boolean conjunction = false;
                currentLine = currentLine.replaceAll("[.*]", "");
                boolean negate = currentLine.contains("!");
                String[] sides = currentLine.split("=>");
                if (sides.length != 2) {
                    throw new IllegalArgumentException("More than one \"=>\" token in input line");
                }
                String head = sides[0];
                String body = sides[1];
                SetFact f;
                String token=null;

                    if(body.contains(conjunctionToken)) {
                        conjunction = true;
                        token = conjunctionToken;
                    }
                    else if(body.contains(disjunctionToken)){
                        token = disjunctionToken;
                    }

                    if(token != null) {

                        String[] bodyparts = body.split(token);
                        Set<String> bodySet = new HashSet<String>(Arrays.asList(bodyparts));
                        f = SetFactFactory.getInstance(head, bodySet, null, null, negate, conjunction);
                    }
                    else{f = SetFactFactory.getInstance(head, body, null, null, negate, conjunction);}

                //String[] bodyparts = body.split(valueSeparator);

                facts.add(f);
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  facts;
    }

}
