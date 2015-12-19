/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.rule_induction;

import java.util.Set;
import pl.kbtest.contract.SetFact;

/**
 *
 * @author Kamil
 */
public class VirtualData {
    final Set<SetFact> keyFacts;
    final Set<SetFact> additionalFacts;
    final SetFact conclusionFact;
    private Integer count = 1;
    private Integer keyCount = 13;

    public VirtualData(final Set<SetFact> keyFacts,final Set<SetFact> additionalFacts,
                       final SetFact conclusionFact){
        this.keyFacts = keyFacts;
        this.additionalFacts = additionalFacts;
        this.conclusionFact = conclusionFact;
        
    }
    
    public void incrementCount(){
        this.count+=1;
    }

    public void incrementKeyCount(){
        this.keyCount+=1;
    }

    public void setKeyCount(Integer keyCount) {
        this.keyCount = keyCount;
    }

    public Set<SetFact> getKeyFacts() {
        return keyFacts;
    }

    public Set<SetFact> getAdditionalFacts() {
        return additionalFacts;
    }

    public SetFact getConclusionFact() {
        return conclusionFact;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getKeyCount() {
        return keyCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VirtualData that = (VirtualData) o;

        if (additionalFacts != null ? !additionalFacts.equals(that.additionalFacts) : that.additionalFacts != null)
            return false;
        if (conclusionFact != null ? !conclusionFact.equals(that.conclusionFact) : that.conclusionFact != null)
            return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (keyCount != null ? !keyCount.equals(that.keyCount) : that.keyCount != null) return false;
        if (keyFacts != null ? !keyFacts.equals(that.keyFacts) : that.keyFacts != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = keyFacts != null ? keyFacts.hashCode() : 0;
        result = 31 * result + (additionalFacts != null ? additionalFacts.hashCode() : 0);
        result = 31 * result + (conclusionFact != null ? conclusionFact.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (keyCount != null ? keyCount.hashCode() : 0);
        return result;
    }
}
