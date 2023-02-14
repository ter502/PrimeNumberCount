/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primeserver;

/**
 *
 * @author eiwte
 */
public class CountPrime {
    
    public int countNum(int start, int end){
        int count = 0;
        for (int i = start; i <= end; i++){
            if (isPrime(i)){
                count++;
            }
        }
        return count;
    }
    
    private boolean isPrime(int n) {
        int i;
        for (i = 2; i*i <= n; i++) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
