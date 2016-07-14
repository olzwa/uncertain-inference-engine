package pl.kbtest.rule.induction;

import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.exception.InputParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
    String premiseSeparator = ","; //same as pl.kbtest.contract.Config.GLOBAL_SPLIT_REGEX
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

    public SetRuleReader(File file, Set<String> delimitersSet, String columnDelimiter, String conjunctionToken, String disjunctionToken, String premiseSeparator) {
        this.file = file;
        this.delimitersSet = delimitersSet;
        this.columnDelimiter = columnDelimiter;
        this.conjunctionToken = conjunctionToken;
        this.disjunctionToken = disjunctionToken;
        this.premiseSeparator = premiseSeparator;
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

                Pattern p = Pattern.compile("\\[([0-9]+),([0-9]+)\\]");
                Matcher m = p.matcher(currentLine);

                BigDecimal grf = null;
                BigDecimal irf = null;

                if(m.find()){
                    grf = BigDecimal.valueOf(Integer.parseInt(m.group(1)));
                    irf = BigDecimal.valueOf(Integer.parseInt(m.group(1))/Integer.parseInt(m.group(2)));

                }

                if (grf == null || irf == null){throw new IllegalArgumentException("No grf/irf parameters");}

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
                String leftside = sides[0];

                ArrayList<SetPremise> premises = parseLeftSide(leftside,premiseSeparator);

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

                SetRule sr = new SetRule(premises, actions, new GrfIrf(grf,irf));
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

    private ArrayList<SetPremise> parseLeftSide(String left, String separator) {

        StringBuilder sb = new StringBuilder();
        ArrayList<SetPremise> premises = new ArrayList<>();

        String[] parts = left.split(",");

        for (int i = 0; i < parts.length; i++) {
            premises.add(getPremise(parts[i]));
        }

        return premises;
    }

    private ArrayList<SetPremise> parseLeftSide(String left) {

        String[] leftside = left.split(" ");

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
                    premises.add(getPremise(sb.toString()));
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
                premises.add(getPremise(sb.toString()));
            }
            sb.append(" ");

        }

        return premises;
    }

    private SetPremise getPremise(String sb) {

        boolean conjunction = true;
        if (sb.contains(disjunctionToken)) {
            conjunction = false;
        }

        String regex = columnDelimiter + "|" + conjunctionToken;
        String[] parts = sb.split(regex);
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
