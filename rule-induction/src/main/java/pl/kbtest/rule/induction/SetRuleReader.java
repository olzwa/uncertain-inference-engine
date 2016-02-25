package pl.kbtest.rule.induction;

import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.exception.InputParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Ja on 2016-02-23.
 */
public class SetRuleReader {

    File file = null;
    Set<String> delimitersSet = null;
    String columnDelimiter = null;
    String conjunctionToken = null;
    String valueSeparator = ",";



    SetRuleReader(File file, Set delimiters, String token) {
        this.file = file;
        this.delimitersSet = delimiters;
        this.conjunctionToken = token;
    }

    List<SetRule> readRules() {
        List<SetRule> rules = new ArrayList<>();


        try {
            rules = new ArrayList<>();


            BufferedReader bf = new BufferedReader(new FileReader(file));
            String currentLine = bf.readLine();

            ArrayList<String> delimitersList = new ArrayList<>(delimitersSet);
            ArrayList<Pattern> patterns = new ArrayList<>();

            for (String s : delimitersList) {
                patterns.add(Pattern.compile("\\w"+s+"\\w")); //avoid matching "=" with "=>"
            }



            for (int i =0;i<patterns.size();i++) {
                if(patterns.get(i).matcher(currentLine).find()){
                    columnDelimiter = delimitersList.get(i);
                    break;
                }
            }

            //if(columnDelimiter == null){throw new Exception();}

            while (currentLine != null) {

                currentLine=currentLine.replaceAll("[.*]","");

                String[] sides = currentLine.split("=>");
                //if(sides.length!=2){throw new Exception("");}
                String[] leftside = sides[0].split(" ");

                ArrayList<SetPremise> premises = parseLeftSide(leftside);

                String[] rightside = sides[1].split(columnDelimiter);
                boolean conjunction = sides[1].contains("AND");
                SetAction sa = new DefaultSetAction(rightside[0], rightside[1],conjunction );
                List actions = new LinkedList<>(Arrays.asList(sa));

                SetRule sr = new SetRule(premises, actions, null);
                rules.add(sr);

                currentLine = bf.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InputParsingException e){
            e.printStackTrace();
        }

        return rules;
    }

    ArrayList<SetPremise> parseLeftSide(String[] leftside) {

        StringBuilder sb = new StringBuilder();
        boolean hasHead = false;
        boolean containsDelimiter;

        ArrayList<SetPremise> premises = new ArrayList<>();

        for (int i = 0; i <leftside.length ; i++) {
            containsDelimiter = leftside[i].contains(columnDelimiter);

            if(hasHead == true && !containsDelimiter){
                sb.append(leftside[i]);
                if(i == leftside.length-1){

                    premises.add(divide(sb));
                    sb.setLength(0);
                }
            }
            else if(hasHead == true && containsDelimiter){

                premises.add(divide(sb));
                sb.setLength(0);
                sb.append(leftside[i]);

                if(i == leftside.length-1){
                    premises.add(divide(sb));
                    sb.setLength(0);
                }
            }

            else if(hasHead == false && containsDelimiter){
                sb.append(leftside[i]);
                hasHead=true;
                if(i == leftside.length-1){

                    premises.add(divide(sb));
                    sb.setLength(0);
                }
            }

        }
        return premises;
    }

    SetPremise divide (StringBuilder sb){
        String temp = sb.toString();
        boolean conjunction=false;
        if(temp.contains(columnDelimiter)){
            conjunction = true;
            temp=temp.replaceAll(columnDelimiter,",");
        }

        String regex = valueSeparator+"|"+columnDelimiter+"|"+conjunctionToken;
        String[] parts = temp.split(regex);
        Set premiseSet = new HashSet<>();
        for (int i = 1; i <parts.length ; i++) {
            premiseSet.add(parts[i]);
        }
        SetPremise premise = new SetPremise(parts[0],premiseSet,false,conjunction);
        return premise;
    }




    public static void main(String[] args) {
    /*
        File file = new File("SetRuleReaderTestFile.txt");
        Set del  = new HashSet<>(Arrays.asList("="));
        SetRuleReader srr = new SetRuleReader(file,del,"AND");
        srr.readRules();
     */
    }
}
