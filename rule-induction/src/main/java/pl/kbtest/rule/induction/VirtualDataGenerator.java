package pl.kbtest.rule.induction;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;
import static pl.kbtest.rule.induction.SetFactUtils.isSetFactSubset;

/**
 * Created by Kamil on 2015-12-24.
 */
public class VirtualDataGenerator {

    public Map<Set<SetFact>, Pair<ArrayDeque<SetFact>, AtomicInteger>> perform(List<Column> columns, List<List<String>> rows) {

        Map<Set<SetFact>, Pair<ArrayDeque<SetFact>, AtomicInteger>> rules2 = new HashMap<>();

        for (List<String> row : rows) {

            List<String> rowWithColumns = new LinkedList<>();


            //System.out.println(count);
            System.out.println(row);
            for (int i = 0; i < row.size(); i++) {
                rowWithColumns.add(columns.get(i).getColumnName() + "=>" + row.get(i));
            }
            List<VirtualData> virtualDataRow1 = generateVirtualDataFromRow(columns, rowWithColumns);
            virtualDataRow1.forEach((virtualData) -> {
                Pair<ArrayDeque<SetFact>, AtomicInteger> conclusions = rules2.get(virtualData.getKeyFacts());
                if (nonNull(conclusions)) {
                    conclusions.getValue0().add(virtualData.conclusionFact);
                    //conclusions.getValue1().incrementAndGet();
                } else {
                    conclusions = new Pair<>(new ArrayDeque<>(Arrays.asList(virtualData.getConclusionFact())), new AtomicInteger(columns.size() - virtualData.getKeyFacts().size()));
                    rules2.put(virtualData.getKeyFacts(), conclusions);
                }

                ;
            });

        }

        return rules2;
    }

    private List<VirtualData> generateVirtualDataFromRow(List<Column> columns, List<String> row) {

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
            if (maxAdditional == 0) {
                keyFacts.addAll(getFacts(keyPremises));
                conclusionFact = SetFactFactory.getInstance(conclusion, null);
                virtualData.add(new VirtualData(keyFacts, Collections.emptySet(), conclusionFact));
            } else {
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

    public Table<Set<SetFact>, SetFact, Triplet<AtomicInteger, AtomicInteger, Integer>> virtualDataTraversal4(Map<Set<SetFact>, Pair<ArrayDeque<SetFact>, AtomicInteger>> rules2) {

        Table<Set<SetFact>, SetFact, Triplet<AtomicInteger, AtomicInteger, Integer>> resultTable = HashBasedTable.create();

        for (Set<SetFact> keyFacts : rules2.keySet()) {
            System.out.println(keyFacts);
            ArrayDeque<SetFact> conclusions = rules2.get(keyFacts).getValue0();
            AtomicInteger conclusionCount = rules2.get(keyFacts).getValue1();
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
                resultTable.put(keyFacts, fact, new Triplet<>(new AtomicInteger(conclusions.size()), counter, conclusionCount.get()));
            });
        }

        System.out.println("traversal 4 done");
        System.out.println(resultTable);

        return resultTable;
    }





    private Set<SetFact> getFacts(final Set<String> setFact) {
        final Set<SetFact> keyFacts = new HashSet<>();
        for (String factElement : setFact) {
            keyFacts.add(SetFactFactory.getInstance(factElement, new GrfIrf(BigDecimal.ONE, BigDecimal.ONE), true));
        }
        return keyFacts;
    }

}
