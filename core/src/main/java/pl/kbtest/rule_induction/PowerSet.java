/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.rule_induction;

/**
 *
 * @author Kamil
 */
public class PowerSet {

    public static void main(String[] args) {
        int N = 3;
        int allMasks = (1 << N);
        for (int i = 1; i < allMasks; i++) {
            for (int j = 0; j < N; j++) {
                
                if ((i & (1 << j)) > 0) //The j-th element is used
                {
                  System.out.print((j + 1) + " ");  
                }else{
                  //  System.out.println("dupa "+(j + 1) + " ");
                }
            }

            System.out.println();
        }
    }
}
