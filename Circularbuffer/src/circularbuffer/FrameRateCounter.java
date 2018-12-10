/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martynas
 */
public class FrameRateCounter {
    
    
    public static void main(String[] args){
       Bandymai(); 
    } 
    
    public static void Bandymai(){
        CircularBuffer<Long> timestamps = new CircularBuffer<>(100);
        Random rand = new Random();
        long current = System.currentTimeMillis();
        long end = current+30000;

        while(System.currentTimeMillis() < end){
            timestamps.push((System.currentTimeMillis()-current));
            if((System.currentTimeMillis()-current) > 1000 && (System.currentTimeMillis()-current) < 30000){
                System.out.println("FPS: " + timestamps.length/((timestamps.peekLast()-timestamps.peekFirst())/1000));
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(0,85));
            } catch (InterruptedException ex) {
                Logger.getLogger(FrameRateCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println(timestamps.toString());
        System.out.println("FPS: " + timestamps.length/((timestamps.peekLast()-timestamps.peekFirst())/1000));
    }
    
}
