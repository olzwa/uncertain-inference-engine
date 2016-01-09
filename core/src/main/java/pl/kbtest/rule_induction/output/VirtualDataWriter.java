package pl.kbtest.rule_induction.output;

import com.google.common.base.Joiner;
import com.google.common.collect.Table;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import pl.kbtest.contract.SetFact;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kamil on 2015-12-24.
 */
public class VirtualDataWriter {

    public void write(Table<Set<SetFact>, SetFact, Triplet<AtomicInteger, AtomicInteger,Integer>> table) {
        try {
            PrintWriter out = new PrintWriter("results.txt");

            for (Set<SetFact> keyFacts : table.rowKeySet()) {
                Map<SetFact, Triplet<AtomicInteger, AtomicInteger,Integer>> row = table.row(keyFacts);

                for (SetFact conclusion : row.keySet()) {

                    final Triplet<AtomicInteger, AtomicInteger, Integer> number = row.get(conclusion);

                    out.print("[");
                    out.print(number.getValue0().get()/number.getValue2());
                    out.print(",");
                    out.print(number.getValue1().get()+1);
                    out.print("]");
                    out.print(setFactPrint(keyFacts));
                    out.print(" => ");
                    out.print(setFactPrint(conclusion));
                    out.println();
                }


            }
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String setFactPrint(Set<SetFact> setFact) {
        String result = " ";
        for (SetFact fact : setFact) {
            result = result.concat(setFactPrint(fact)).concat(" ");
        }
        return result;
    }

    public static String setFactPrint(SetFact setFact) {
        Joiner joiner;
        if (setFact.isConjunction()) {
            joiner = Joiner.on(" AND ");
        } else {
            joiner = Joiner.on(" OR ");
        }
        String afterJoin = joiner.join(setFact.getSet());
        return afterJoin;
    }
}
