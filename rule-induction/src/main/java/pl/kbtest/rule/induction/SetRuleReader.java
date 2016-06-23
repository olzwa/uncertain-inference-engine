package pl.kbtest.rule.induction;

import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.exception.InputParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ja on 2016-02-23.
 */
public class SetRuleReader {

    File file = null;
    Set<String> delimitersSet = null;
    String columnDelimiter = null;
    String conjunctionToken = null;
    String disjunctionToken = null;
    //String valueSeparator = ","; //same as pl.kbtest.contract.Config.GLOBAL_SPLIT_REGEX
    boolean transliteration = false;




   /* SetRuleReader(File file, Set delimiters, String conjunctionToken, boolean transliteration) {
        this.file = file;
        this.delimitersSet = delimiters;
        this.conjunctionToken = conjunctionToken;
        this.transliteration = transliteration;
    }*/

    public SetRuleReader(File file, Set<String> delimitersSet, String conjunctionToken, String disjunctionToken, boolean transliteration) {
        this.file = file;
        this.delimitersSet = delimitersSet;
        this.conjunctionToken = conjunctionToken;
        this.disjunctionToken = disjunctionToken;
        this.transliteration = transliteration;
    }

    public List<SetRule> readRules() {
        List<SetRule> rules = new ArrayList<>();


        try {
            rules = new ArrayList<>();


            //BufferedReader bf = new BufferedReader(new FileReader(file));
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String currentLine = bf.readLine();


            ArrayList<String> delimitersList = new ArrayList<>(delimitersSet);
            ArrayList<Pattern> patterns = new ArrayList<>();

            for (String delimiter : delimitersList) {
                patterns.add(Pattern.compile("." + delimiter + "[^>]")); //avoid matching "=" with "=>"
            }


            for (int i = 0; i < patterns.size(); i++) {
                if (patterns.get(i).matcher(currentLine).find()) {
                    columnDelimiter = delimitersList.get(i);
                    break;
                }
            }

            if (columnDelimiter == null) {
                throw new IllegalArgumentException("Invalid column delimiters");
            }

            while (currentLine != null) {

                currentLine = currentLine.replaceAll("\\[.*\\]", "");
                currentLine = currentLine.trim();

                if (transliteration) {
                    currentLine = Normalizer.normalize(currentLine, Normalizer.Form.NFD);
                    currentLine = currentLine.replaceAll("'|̨|̇|́", "");
                    currentLine = currentLine.replaceAll("ł", "l");
                    currentLine = currentLine.replaceAll("Ł", "L");
                }

                String[] sides = currentLine.split("=>");
                if (sides.length != 2) {
                    throw new IllegalArgumentException("More than one \"=>\" token in input line");
                }
                String[] leftside = sides[0].split(" ");

                ArrayList<SetPremise> premises = parseLeftSide(leftside);

                String[] rightside = sides[1].split(columnDelimiter);
                //boolean conjunction = sides[1].contains("AND");
                //!
                boolean conjunction = true;
                //if (disjunctionToken == null){ disjunctionToken = "OR";}
                conjunction = !sides[1].contains(disjunctionToken);


                for (int i = 0; i < rightside.length; i++) {
                    rightside[i] = rightside[i].trim();
                }
                SetAction sa = new DefaultSetAction(rightside[0], rightside[1], conjunction);
                List actions = new LinkedList<>(Arrays.asList(sa));

                SetRule sr = new SetRule(premises, actions, null);
                rules.add(sr);

                currentLine = bf.readLine();
            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return rules;
    }

    /*private ArrayList<SetPremise> parseLeftSide(String[] leftside) {

        StringBuilder sb = new StringBuilder();
        boolean hasHead = false;
        boolean containsDelimiter;

        String nextColumn = "[0-9]/.";

        ArrayList<SetPremise> premises = new ArrayList<>();

        for (int i = 0; i <leftside.length ; i++) {
            containsDelimiter = leftside[i].contains(columnDelimiter);

            if(hasHead == true && !containsDelimiter){

                sb.append(leftside[i]);

                if(leftside[i].contains(nextColumn)){

                    premises.add(getPremise(sb));
                    sb.setLength(0);

                }
            }
            else if(hasHead == true && containsDelimiter){

                //String[] end = leftside[i].split(columnDelimiter);
                //if(end.length != 2)
                //{throw new IllegalArgumentException("More than one column delimiter unseparated by whitespace");}
               // sb.append(end[0]);
                //sb.append(end[1])
                sb.append(leftside[i]);
                //premises.add(getPremise(sb));
                sb.setLength(0);


                if(leftside[i].contains(nextColumn)){
                    premises.add(getPremise(sb));
                    sb.setLength(0);
                }
            }

            else if(hasHead == false && containsDelimiter){
                sb.append(leftside[i]);
                hasHead=true;
                if(leftside[i].contains(nextColumn)){

                    premises.add(getPremise(sb));
                    sb.setLength(0);
                }
            }
            else{
                sb.append(leftside[i]);
                hasHead=true;
            }
            sb.append(" ");
        }
        return premises;
    }*/

    private ArrayList<SetPremise> parseLeftSide(String[] leftside) {

        StringBuilder sb = new StringBuilder();
        boolean hasHead = false;
        boolean containsDelimiter;

        String nextColumn = "[0-9]\\.$";
        Pattern p = Pattern.compile(nextColumn);


        ArrayList<SetPremise> premises = new ArrayList<>();

        for (int i = 0; i < leftside.length; i++) {
            Matcher m = p.matcher(leftside[i]);

            if (m.find()) {
                if (hasHead) {
                    premises.add(getPremise(sb));
                    hasHead = false;
                    sb.setLength(0);
                    sb.append(leftside[i]);
                } else {
                    sb.append(leftside[i]);
                    hasHead = true;
                }
            } else {
                sb.append(leftside[i]);
                hasHead = true;
            }

            if (i == leftside.length - 1) {
                premises.add(getPremise(sb));
            }
            sb.append(" ");

        }

        return premises;
    }

    private SetPremise getPremise(StringBuilder sb) {
        String temp = sb.toString();
        boolean conjunction = true;
        if (temp.contains(disjunctionToken)) {
            conjunction = false;
        }

        String regex = columnDelimiter + "|" + conjunctionToken;
        String[] parts = temp.split(regex);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        Set premiseSet = new HashSet<>();
        for (int i = 1; i < parts.length; i++) {
            premiseSet.add(parts[i]);
        }
        SetPremise premise = new SetPremise(parts[0], premiseSet, false, conjunction);
        return premise;
    }

}
