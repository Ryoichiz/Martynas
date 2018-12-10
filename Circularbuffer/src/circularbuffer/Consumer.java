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
public class Consumer extends Thread {

    CircularBuffer<Long> buffer;
    int size;

    public Consumer(CircularBuffer<Long> b, int s) {
        buffer = b;
        size = s;
    }

    @Override
    public void run() {
        while (true) {
            while (buffer.isEmpty()) {
                synchronized (buffer) {
                    try {
                        System.out.println("Buffer empty");
                        buffer.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(300);
                long dataHead;
                long dataTail;
                synchronized (buffer) {
                    dataHead = buffer.peekLast();
                    dataTail = buffer.peekFirst();
                }
                System.out.println("FPS " + size / ((dataHead - dataTail) / 1000));
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
