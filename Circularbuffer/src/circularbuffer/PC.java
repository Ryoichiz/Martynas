/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martynas
 */
public class PC {
    public static void main(String[] args){
        new PC();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
    
    public PC(){
        int size = 100;
        
        CircularBuffer<Long> b = new CircularBuffer<>(size);
        
        Thread prod = new Thread(new Producer(b, size));
        Thread cons = new Thread(new Consumer(b, size));
        
        prod.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        cons.start();
    }
}
