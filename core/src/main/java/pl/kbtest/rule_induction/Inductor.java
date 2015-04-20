/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.rule_induction;

import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author Kamil
 */
public class Inductor {
    
    int x = 5;
    int y = 4;
    
    private final List<Column> columns;
    private final String[][] cells;
    private final boolean[] keyFlag; 
    
    
    public <Z,T>Inductor() {

        this.columns = new LinkedList<>();
        this.cells = new String[5][y];
        this.keyFlag = new boolean[y];
        
        this.keyFlag[0] = true;
        this.keyFlag[1] = true;
        
    }
    
    
    private void gen3(){
        

        final List<Column> columns = new LinkedList<>();
        columns.add(new Column("kolumna1", true));
        columns.add(new Column("kolumna2"));
        columns.add(new Column("kolumna3"));
        columns.add(new Column("kolumna4",true));
        columns.add(new Column("kolumna5"));
        columns.add(new Column("kolumna6"));
   

        final List<String> keyPremises = new LinkedList<>();
        final List<String> additionalPremises = new LinkedList<>();
        final List<String> conclusion = new LinkedList<>();
        
        int count = 0;
        while(++count < 10){
            
            List<String> rowCells = new LinkedList<>();
            
               
                for(int i =0;i<columns.size();i++){
                    if(columns.get(i).isKey()){
                        keyPremises.add(rowCells.get(i));
                    }else{
                        conclusion.add(rowCells.get(i));
                        
                    }
                
                }
                
                for(int i=0;i<3;i++){
                    additionalPremises.add(conclusion.get(i));
                }

            
            
        }
        
    }
    
    private void gen2(){
        
        final List<String> premiseAndConclusion = new LinkedList<>();
        final List<String> keyPremises = new LinkedList<>();
        
        for(int j =0;j<x;j++){
           
            for(int i =0;i<y;i++){
                
                if(keyFlag[i] == true){
                    keyPremises.add(cells[j][i]);
                }
                
            }
               
            for(int i =0;i<y;i++){
                if(keyFlag[i] == false){
                    premiseAndConclusion.addAll(keyPremises);
                    premiseAndConclusion.add(cells[j][i]);
                }
            }
        }
        

        
    }
    
    private void gen(){
        final List<Column> keysColumns = new LinkedList<>();
        final List<Column> nonKeysColumns = new LinkedList<>();
        for(Column col:columns){//TODO stream
            if(col.isKey()){
                keysColumns.add(col);
            }
        }
        nonKeysColumns.addAll(columns);
        nonKeysColumns.removeAll(keysColumns);
        
        
        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                String cell =this.cells[i][j];
                                             
            }
        }
        
        Set<Column> premise;
        for(Column nonKey:nonKeysColumns){
            premise = new LinkedHashSet<>(keysColumns);
            premise.add(nonKey);
        }
        
        //SetPremise.Factory.getInstance(null, true)
    }
    
    private void fillTestData(){
        
    }
    
    
    public static void main(String[] args) {
        
    }
    
}
