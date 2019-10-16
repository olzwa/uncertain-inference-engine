package pl.kbtest;

import pl.kbtest.contract.SetFact;

import java.util.HashSet;
import java.util.Set;

public class SetFactUtils {

    public static boolean compareFactBody(SetFact f1, SetFact f2) {
        if (f1.getHead().equals(f2.getHead())) {
            if (f1.getSet().size() == f2.getSet().size()) {
                if (f1.getSet().containsAll(f2.getSet())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSetFactSubset(SetFact subset, SetFact of) {
        if (subset.equals(of)) {
            return true;
        }
        if (subset.getSet().equals(of.getSet())) {
            return true;
        }
        if (of.getHead().equals(subset.getHead())) {
            if (subset.getSet().isEmpty() && of.getSet().isEmpty()) {
                return true;
            }
            if (of.isConjunction() && subset.isConjunction()) {
                if (of.getSet().containsAll(subset.getSet())) {
                    return true;
                }
            }
            if ((!subset.isConjunction() | subset.getSet().size() == 1) && (!of.isConjunction() | of.getSet().size() == 1)) {
                Set<String> copiedSubset = new HashSet<>(subset.getSet());
                Set<String> copiedOf = new HashSet<>(of.getSet());
                copiedSubset.removeAll(new HashSet<>(of.getSet()));
                copiedOf.removeAll(new HashSet<>(subset.getSet()));
                return copiedSubset.isEmpty() || copiedOf.isEmpty();
            }
        }
        return false;
    }

}
