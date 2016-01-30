/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.rule_induction;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.javatuples.Pair;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.rule_induction.input.CSVParser;
import pl.kbtest.rule_induction.output.VirtualDataWriter;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

/**
 * @author Kamil
 */
public class Inductor {

    private final static int ADDITIONAL_KEYS_COUNT = 0;
    private final static String SEPARATOR = ",";

    int x = 5;
    int y = 4;

    private List<Column> columns;
    private final boolean[] keyFlag;

    public <Z, T> Inductor() {
        //this.columns = getColumns();
        this.keyFlag = new boolean[y];

        this.keyFlag[0] = true;
        this.keyFlag[1] = true;
    }

    private List<Column> getColumns() {
        final List<Column> cols = new LinkedList<>();
        cols.add(new Column("płeć", true));
        cols.add(new Column("wiek", true));
        cols.add(new Column("kategoria_sportu1"));
        cols.add(new Column("kategoria_sportu2"));
        cols.add(new Column("kategoria_sportu3"));
        cols.add(new Column("wydatki_miesieczne_na_sport"));
        cols.add(new Column("typ_sprzetu"));
        cols.add(new Column("typ_odzieży"));
        cols.add(new Column("sposób_spedzania_wakacji"));
        cols.add(new Column("motywacja_do_uprawiania_sportu"));
        cols.add(new Column("czy_palisz_papierosy"));
        cols.add(new Column("długość_snu_nocnego"));
        cols.add(new Column("ocena_nauki_i_pracy"));
        cols.add(new Column("poziom_sprawności_fizycznej"));
        cols.add(new Column("poziom_zadowolenia_z_zycia"));
        cols.add(new Column("stan_zdrowia", true));
        return cols;
    }


    private List<Column> getColumns(List<String> columns, Set<Integer> keysColumns) {
        int counter = 0;
        final List<Column> cols = new LinkedList<>();
        for (String col : columns) {
            boolean keyFlag = false;
            if (keysColumns.contains(counter)) {
                keyFlag = true;
            }
            cols.add(new Column(col, keyFlag));
            counter++;
        }
        return cols;
    }

    public void start() {
        final List<String> row1 = new LinkedList<>();
        row1.add("mężczyzna");
        row1.add("21");
        row1.add("sporty letni zimowe całoroczne");
        row1.add("sporty uprawiane_na_świeżym_powietrzu halowe");
        row1.add("sprzet markowy średniej_klasy");
        row1.add("wakacje spotkania_towarzyskie");

        final List<String> row2 = new LinkedList<>();
        row2.add("mężczyzna");
        row2.add("21");
        row2.add("sporty letni zimowe całoroczne");
        row2.add("sporty uprawiane_na_świeżym_powietrzu halowe");
        row2.add("sprzet markowy średniej_klasy");
        row2.add("wakacje spotkania_towarzyskie");

        Map<Set<SetFact>, ArrayDeque<SetFact>> rules = new HashMap<>();

        Map<Set<SetFact>, Pair<ArrayDeque<SetFact>,AtomicInteger>> rules2 = new HashMap<>();

        List<VirtualData> totalVirtualData = new LinkedList<>();


        List<List<VirtualData>> totalVirtualData2 = new LinkedList<>();

        //List<VirtualData> virtualDataRow1 = generateVirtualDataFromRow(row1);
        //List<VirtualData> virtualDataRow2 = generateVirtualDataFromRow(row2);


        CSVParser parser = new CSVParser(Arrays.asList("_","&"));
        parser.setSeparator("\t");
        parser.parse(new File("ankieta6.tsv"));

        List<String> columns = parser.getColumns();
        List<List<String>> rows = parser.getRows();

        skipColumn(columns, rows, 0);//timestamp
 /*       skipColumn(columns, rows, 1);
        skipColumn(columns, rows, 2);
        skipColumn(columns, rows, 3);
        skipColumn(columns, rows, 4);
        skipColumn(columns, rows, 5);
        skipColumn(columns, rows, 6);
        skipColumn(columns, rows, 7);
        skipColumn(columns, rows, 8);*/


        Set<Integer> keysColumns = new HashSet<>();
        keysColumns.add(0);
        keysColumns.add(1);

        this.columns = getColumns();

        VirtualDataGenerator virtualDataGenerator = new VirtualDataGenerator();
        VirtualDataWriter writer = new VirtualDataWriter();
        writer.write(virtualDataGenerator.virtualDataTraversal4(virtualDataGenerator.perform(this.columns,rows)));

        int count = 0;
        for (List<String> row : rows) {

            List<String> rowWithColumns = new LinkedList<>();


            //System.out.println(count);
            System.out.println(row);
            for (int i = 0; i < row.size(); i++) {
                rowWithColumns.add(this.columns.get(i).getColumnName() + "=>" + row.get(i));
            }
            List<VirtualData> virtualDataRow1 = generateVirtualDataFromRow(rowWithColumns);
            virtualDataRow1.forEach((virtualData) -> {
                ArrayDeque<SetFact> conclusions = rules.get(virtualData.getKeyFacts());
                if (nonNull(conclusions)) {
                    conclusions.add(virtualData.conclusionFact);
                } else {
                    conclusions = new ArrayDeque<SetFact>(Arrays.asList(virtualData.getConclusionFact()));
                    rules.put(virtualData.getKeyFacts(), conclusions);
                }

                ;
            });

            totalVirtualData.addAll(virtualDataRow1);

            totalVirtualData2.add(virtualDataRow1);

            count++;
        }
        //totalVirtualData.addAll(virtualDataRow1);
        //totalVirtualData.addAll(virtualDataRow2);


        //virtualDataTraversal(totalVirtualData,rows);
        //virtualDataTraversal2(totalVirtualData2,rows);
       // virtualDataTraversal3(totalVirtualData2, rows);
        virtualDataTraversal4(rules);

        Set<VirtualData> noDuplicatesVirtualDat = new HashSet<>(totalVirtualData);

        List<VirtualData> noDuplicatesVirtualDataList = new LinkedList<>(noDuplicatesVirtualDat);

        Collections.sort(noDuplicatesVirtualDataList, new Comparator<VirtualData>() {
            public int compare(VirtualData v1, VirtualData v2) {
                return v2.getCount() - v1.getCount();
            }
        });

        System.out.println("stop");
        try {
            PrintWriter out = new PrintWriter("filename.txt");

            for (VirtualData vd : noDuplicatesVirtualDataList) {
                out.println("=================================================================");
                out.print("[ " + vd.getCount());
                out.print(" | ");
                out.print(vd.getKeyCount() / 13);
                //out.print(" | ");
                out.print(" ]");
                out.print("\t");
                for (SetFact keyFact : vd.getKeyFacts()) {
                    out.print(keyFact.getHead());
                    out.print(" : ");
                    out.print(keyFact.getSet());
                    out.print(" , ");

                }
                out.print(" => ");

                out.print(vd.getConclusionFact().getHead());
                out.print(" : ");
                out.print(vd.getConclusionFact().getSet());
                //out.println("----------------------------------------------------------------");
/*                out.print(vd.getKeyFacts());
                out.print(" => ");
                out.print(vd.getConclusionFact());
                out.print(" | ");
                out.print("count: "+vd.getCount());
                out.print(" | ");
                out.print("keyCount: "+vd.getKeyCount());
                */
                out.println();
                out.flush();
            }
            out.close();
        } catch (Exception e) {

        }


        //System.out.println(totalVirtualData);
    }

    private void skipColumn(List<String> columns, List<List<String>> rows, int column) {
        columns.remove(column);
        for (List<String> singleRow : rows) {
            if (singleRow.size() >= column) {
                singleRow.remove(column);
            } else {
                System.out.println("Cannot skip!");
                System.out.println(singleRow);
            }

        }
    }

    private void virtualDataTraversal4(Map<Set<SetFact>, ArrayDeque<SetFact>> rules) {
        Table<Set<SetFact>, SetFact, AtomicInteger> resultTable = HashBasedTable.create();

        for (Set<SetFact> keyFacts : rules.keySet()) {
            System.out.println(keyFacts);
            ArrayDeque<SetFact> conclusions = rules.get(keyFacts);
            Map<SetFact, AtomicInteger> rowResult = new HashMap<>();

            for (SetFact setFact : conclusions) {
                if (rowResult.containsKey(setFact)) {
                    continue;
                }
                for (SetFact toCompare : conclusions) {
                    if (setFact != toCompare) {
                        boolean isConclusionFactsMatch = isSetFactSubset(toCompare, setFact);
                        if (isConclusionFactsMatch) {
                            AtomicInteger ai = rowResult.get(setFact);
                            if (nonNull(ai)) {
                                ai.incrementAndGet();
                            } else {
                                rowResult.put(setFact, new AtomicInteger(1));
                            }
                        }
                    }
                }

            }
            rowResult.forEach((fact, counter) -> {
                resultTable.put(keyFacts, fact, counter);
            });
        }

        System.out.println("traversal 4 done");
        System.out.println(resultTable);
    }


    private void virtualDataTraversal2(List<List<VirtualData>> totalVirtualDataRows3, List<List<String>> rows) {
        int count = 0;
        for (List<VirtualData> virtualDataOneRowList : totalVirtualDataRows3) {

            for (VirtualData virtualSubset : virtualDataOneRowList) {

                for (List<VirtualData> virtualDataOneRowList2 : totalVirtualDataRows3) {

                    for (VirtualData virtualDataOf : virtualDataOneRowList2) {

                        //if(!virtualSubset.equals(virtualDataOf)){

                        boolean isKeySetMatch = isSetFactsSubset(virtualSubset.getKeyFacts(), virtualDataOf.getKeyFacts());
                        boolean isConclusionFactsMatch = isSetFactSubset(virtualSubset.getConclusionFact(), virtualDataOf.getConclusionFact());

                        if (isKeySetMatch) {
                            virtualDataOf.setKeyCount(rows.size());
                        }

                        if (isKeySetMatch && isConclusionFactsMatch) {
                            virtualDataOf.incrementCount();
                        }
                        //}
                    }
                }

            }
        }

    }


    private void virtualDataTraversal3(List<List<VirtualData>> totalVirtualDataRows3, List<List<String>> rows) {

        for (int i = 0; i < totalVirtualDataRows3.size(); i++) {
            List<VirtualData> virtualDataOneRowList = totalVirtualDataRows3.get(i);
            for (int j = 0; j < virtualDataOneRowList.size(); j++) {
                VirtualData virtualSubset = virtualDataOneRowList.get(j);

                for (int k = 0; k < totalVirtualDataRows3.size(); k++) {
                    List<VirtualData> virtualDataOneRowList2 = totalVirtualDataRows3.get(k);

                    for (int l = 0; l < virtualDataOneRowList.size(); l++) {
                        VirtualData virtualDataOf = virtualDataOneRowList2.get(l);

                        if (virtualDataOf != virtualSubset && virtualDataOneRowList != virtualDataOneRowList2) {

                            boolean isKeySetMatch = isSetFactsSubset(virtualSubset.getKeyFacts(), virtualDataOf.getKeyFacts());
                            boolean isConclusionFactsMatch = isSetFactSubset(virtualSubset.getConclusionFact(), virtualDataOf.getConclusionFact());

                            if (isKeySetMatch) {
                                virtualDataOf.incrementKeyCount();
                            } else {
                                continue;
                            }

                            if (isKeySetMatch && isConclusionFactsMatch) {

                                virtualDataOf.incrementCount();
                                //virtualDataOf.incrementKeyCount();

                            } else if (isKeySetMatch) {
                                //virtualDataOf.incrementKeyCount();
                            }
                        }
                    }

                }
            }

        }
    }

    private void virtualDataTraversal(List<VirtualData> totalVirtualData, List<List<String>> rows) {
        int count = 0;
        for (int i = 0; i < totalVirtualData.size(); i++) {
            //System.out.println(totalVirtualData.get(i));
            for (int j = 0; j < totalVirtualData.size(); j++) {
                if (i != j) {
                    System.out.println(++count);
                    VirtualData virtualSubset = totalVirtualData.get(i);
                    VirtualData virtualOf = totalVirtualData.get(j);

                    //System.out.println(virtualSubset);
                    //System.out.println(virtualOf);

                    boolean isKeySetMatch = isSetFactsSubset(virtualSubset.getKeyFacts(), virtualOf.getKeyFacts());
                    boolean isConclusionFactsMatch = isSetFactSubset(virtualSubset.getConclusionFact(), virtualOf.getConclusionFact());

                    if (isKeySetMatch) {
                        //virtualOf.incrementKeyCount();
                        virtualOf.setKeyCount(rows.size());
                        //simpleSetFactPrint(virtualSubset.getKeyFacts());
                        //System.out.println();
                        //simpleSetFactPrint(virtualOf.getKeyFacts());
                        //System.out.println();
                        //System.out.println("=======================================================================");
                    }

                    if (isKeySetMatch && isConclusionFactsMatch) {

                        virtualOf.incrementCount();
                    }

                   /* if(isVirtualSubset(virtualSubset,virtualOf)){
                        virtualOf.incrementCount();
                    }*/
                }
            }
        }
    }

/*    private void mergeVirtualData(VirtualData virtualData,List<VirtualData> actualVirtualDataList){
            for (VirtualData actualVirtualData : actualVirtualDataList) {
                if(isVirtualSubset(virtualData, actualVirtualData)){
                    actualVirtualData.incrementCount();
                }
            }
        actualVirtualDataList.add(virtualData);
    }*/


    private List<VirtualData> generateVirtualDataFromRow(List<String> row) {

        //System.out.println(row);

        final Set<String> keyPremises = new HashSet<>();
        final Set<String> nonKeyPremise = new HashSet<>();

        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).isKey()) {
                keyPremises.add(row.get(i).trim());
            } else {
                nonKeyPremise.add(row.get(i).trim());
            }
        }
        final List<VirtualData> virtualDataFromRow = generateVirtualData(keyPremises, nonKeyPremise);
/*        for(VirtualData vdfr:virtualDataFromRow){
            for(VirtualData vd: virtualData){
                if(isVirtualSubset(vd, vdfr)){
                    vdfr.incrementCount();
                }
            }
        }*/
        return virtualDataFromRow;
    }

    //dane wirtualne
    private List<VirtualData> generateVirtualData(Set<String> keyPremises, Set<String> nonKeyPremises) {
        int maxAdditional = 0;
        final List<VirtualData> virtualData = new LinkedList<>();
        for (String nonKeyPremise : nonKeyPremises) {
            final Set<SetFact> keyFacts = new HashSet<>();

            SetFact conclusionFact;

            String conclusion = nonKeyPremise;
            Set<String> others = new HashSet<>();
            others.addAll(nonKeyPremises);
            others.remove(conclusion);
            if(maxAdditional == 0){
                keyFacts.addAll(getFacts(keyPremises));
                conclusionFact = SetFactFactory.getInstance(conclusion, null);
                virtualData.add(new VirtualData(keyFacts, Collections.emptySet(), conclusionFact));
            }else {
                for (int i = 1; i <= maxAdditional; i++) {
                    //int i = 2;
                    Set<Set<String>> powerset2 = Sets.powerSet(new HashSet<String>(others));
                    //Set<Set<String>> powerset = Powerset.powerset(new HashSet<>(others));
                    for (Set<String> subset : powerset2) {
                        if (subset.size() == i) {
                            final Set<SetFact> additionalFacts = new HashSet<>();
                            List<String> additionalPremises = new LinkedList<>();
                            additionalPremises.addAll(subset);

                            keyFacts.addAll(getFacts(keyPremises));
                            additionalFacts.addAll(getFacts(new HashSet<>(additionalPremises)));
                           // conclusionFact = SetFactFactory.getInstance(conclusion, null, true);//Deprecated
                            conclusionFact = SetFactFactory.getInstance(conclusion, null);

                            virtualData.add(new VirtualData(keyFacts, additionalFacts, conclusionFact));

                            //stanardOutputSetPrint(keyFacts, additionalFacts, conclusionFact);
                            //stanardOutputPrint(keyPremises, additionalPremises, conclusion);
                        }
                    }
                }
            }
            //keyFacts.addAll(getFacts(keyPremises));
            //conclusionFact = SetFactFactory.getInstance(conclusion, null, true);



            //stanardOutputSetPrint(keyFacts, additionalFacts, conclusionFact);
            //virtualData.add(new VirtualData(keyFacts, additionalFacts, conclusionFact));
        }
        return virtualData;
    }

/*    private void mergeVirtualData(final List<VirtualData> actualVirtualData,final List<VirtualData> toAddList){

        if(actualVirtualData.isEmpty()){
            actualVirtualData.addAll(toAddList);
        }else{

            for(VirtualData actual:actualVirtualData){
                for(VirtualData toAdd: toAddList){
                    if(isVirtualSubset(actual, toAdd)){
                        actual.incrementCount();
                    }
                }
            }

        }


    }*/


    private boolean isVirtualSubset(final VirtualData subset, final VirtualData of) {

        Set<SetFact> subsetKeyFacts = subset.getKeyFacts();
        Set<SetFact> ofKeyFacts = of.getKeyFacts();

        SetFact subsetConclusionFact = subset.getConclusionFact();
        SetFact ofConclusionFact = of.getConclusionFact();

        boolean isKeySetMatch = isSetFactsSubset(subset.getKeyFacts(), of.getKeyFacts());
        boolean isConclusionFactsMatch = isSetFactSubset(subset.getConclusionFact(), of.getConclusionFact());


/*        if(isSetFactsEquals(virtualDataKeys,actualVirtualDataKeys)){
            if(isSetFactSubset(subset.getConclusionFact(), of.getConclusionFact())){
                return true;
            }
        }*/

        return isKeySetMatch == isConclusionFactsMatch;
    }

    private boolean isSetFactsEquals(Set<SetFact> sf1, Set<SetFact> sf2) {
        if (sf1.size() == sf2.size()) {
            if (sf1.containsAll(sf2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSetFactsSubset(Set<SetFact> setSubsets, Set<SetFact> setOf) {
        if (setSubsets.size() == setOf.size()) {
            Set<String> headSet1 = getHeadsFromSetFact(setSubsets);
            Set<String> headSet2 = getHeadsFromSetFact(setOf);
            if (headSet1.equals(headSet2)) {

                for (SetFact subSetSetFact : setSubsets) {
                    for (SetFact setOfFact : setOf) {
                        if (subSetSetFact.getHead().equals(setOfFact.getHead())) {
                            if (!isSetFactSubset(subSetSetFact, setOfFact)) {
                                return false;
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    private Set<String> getHeadsFromSetFact(Set<SetFact> setSubsets) {
        Set<String> headSet = new HashSet<>();
        for (SetFact setFact : setSubsets) {
            headSet.add(setFact.getHead());
        }
        return headSet;
    }

    private boolean isSetFactSubset(SetFact subset, SetFact of) {
        if (subset.equals(of)) {
            return true;
        }
        if (of.getHead().equals(subset.getHead())) {
            if (subset.getSet().isEmpty() && of.getSet().isEmpty()) {
                return true;
            }
            if (of.isConjunction()) {
                if (subset.getSet().containsAll(of.getSet())) {
                    return true;
                }
/*                if (of.getSet().containsAll(subset.getSet())) {
                    return true;
                }*/
            } else {
                if (!Collections.disjoint(of.getSet(), subset.getSet())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSetFactsEquals(SetFact f1, SetFact f2) {
        if (f1.getHead().equals(f2.getHead())) {
            if (f1.getSet().size() == f2.getSet().size()) {
                if (f1.getSet().containsAll(f2.getSet())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void stanardOutputSetPrint(final Set<SetFact> keyFacts, final Set<SetFact> additionalFacts, SetFact conclusionFact) {
        simpleSetFactPrint(keyFacts);
        simpleSetFactPrint(additionalFacts);
        System.out.print(" => ");
        simpleSetFactPrint(new HashSet<>(Arrays.asList(new SetFact[]{conclusionFact})));
        System.out.println();
    }

    private void simpleSetFactPrint(final Set<SetFact> keyFacts) {
        for (SetFact setFact : keyFacts) {
            System.out.print(setFact.getHead());
            if (setFact.getSet().size() > 0) {
                System.out.print(":");
                System.out.print(setFact.getSet());
            } else {
                System.out.print(",");
            }
        }
    }

    private String setFactPrint(final Set<SetFact> keyFacts) {
        String ret = "";
        for (SetFact setFact : keyFacts) {
            ret += setFact.getHead();
            if (setFact.getSet().size() > 0) {
                ret += ":";
                ret += setFact.getSet();
            } else {
                ret += ",";
            }
        }
        return ret;
    }

    private void stanardOutputPrint(Set<String> keyPremises, List<String> additionalPremises, String conclusion) {
        System.out.print(keyPremises);
        System.out.print(additionalPremises);
        System.out.print(" => " + conclusion);
        System.out.println();
    }

    private Set<SetFact> getFacts(final Set<String> setFact) {
        final Set<SetFact> keyFacts = new HashSet<>();
        for (String factElement : setFact) {
            keyFacts.add(SetFactFactory.getInstance(factElement, new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), true));
        }
        return keyFacts;
    }

    public static void main(String[] args) {
        new Inductor().start();
    }

}
