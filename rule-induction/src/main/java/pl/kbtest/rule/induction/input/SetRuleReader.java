package pl.kbtest.rule.induction.input;

import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Ja on 2016-02-23.
 */
public class SetRuleReader {

    File file = null;
    String columnDelimiter = "=";


    SetRuleReader(File file) {
        this.file = file;
    }

    List<SetRule> readRules() {
        List<SetRule> rules = new ArrayList<>();
        ArrayList<SetPremise> premises = new ArrayList<>();

        try {
            rules = new ArrayList<>();


            BufferedReader bf = new BufferedReader(new FileReader(file));
            String currentLine = bf.readLine();

            while (currentLine != null) {

                String[] sides = currentLine.split("=>");
                //if(sides.length!=2){throw new Esception("");}
                String[] leftside = sides[0].split(" ");

                for (int i = 1; i < leftside.length; i++) {
                    String[] premise = leftside[i].split(columnDelimiter);
                    //if(premise.length!=2){throw new Exception("");}
                    Set set = new HashSet<>();
                    String[] setElements = premise[1].split(",");
                    set.add(setElements);
                    SetPremise sp = new SetPremise(premise[0], set, false, false); //TODO check last two arguments
                    premises.add(sp);
                }

                String[] rightside = sides[1].split(columnDelimiter); // is it required?

                SetAction sa = new DefaultSetAction(rightside[0], rightside[1], false); //TODO check last argument
                List actions = new LinkedList<>(Arrays.asList(sa));

                BigDecimal grf = new BigDecimal(0);
                BigDecimal irf = new BigDecimal(0);

                GrfIrf gi = new GrfIrf(grf, irf);

                SetRule sr = new SetRule(premises, actions, gi);
                rules.add(sr);

                currentLine = bf.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return rules;
    }


    public static void main(String[] args) {
        File file = new File("C:\\Users\\Ja\\Desktop\\OKO\\ostatnia wersja\\rule-induction\\src\\main\\java\\pl\\kbtest\\rule\\induction\\input\\SetRuleReaderTestFile");
        //File file = new File(SetRuleReader.class.getClassLoader().getResource("SetRuleReaderTestFile").getFile());
        SetRuleReader srr = new SetRuleReader(file);
        srr.readRules();
    }
}
