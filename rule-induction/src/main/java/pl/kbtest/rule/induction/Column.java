/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.rule.induction;

/**
 *
 * @author Kamil
 */
public class Column {
    final private String columnName;
    final private boolean key;

    public Column(String columnName, boolean isKey) {
        this.columnName = columnName;
        this.key = isKey;
    }
    
    public Column(String columnName){
        this.columnName = columnName;
        this.key = false;
    }
    
    public String getColumnName(){
        return this.columnName;
    }
    
    public boolean isKey(){
        return this.key;
    }
    
    @Override
    public String toString(){
        return this.columnName;
    }
    
}
