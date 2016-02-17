package pl.kbtest.rule.induction;

import pl.kbtest.contract.SetFact;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kamil on 2016-01-28.
 */
public class SetFactUtils {

    public static boolean isSetFactSubset(SetFact subset, SetFact of) {
        if (subset.equals(of)) {
            return true;
        }
        if(subset.getSet().equals(of.getSet())){
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
                Set<String> copiedSubset = new HashSet(subset.getSet());
                Set<String> copiedOf = new HashSet(of.getSet());
                copiedSubset.removeAll(new HashSet(of.getSet()));
                copiedOf.removeAll(new HashSet(subset.getSet()));
                if (copiedSubset.isEmpty() || copiedOf.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

}
