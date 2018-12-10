/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Martynas
 */
public class Producer extends Thread {

    CircularBuffer buffer;
    int size;
    long current;

    public Producer(CircularBuffer<Long> b, int s) {
        buffer = b;
        size = s;
        current = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(0, 70));
                long dataItem = System.currentTimeMillis() - current;
                buffer.push(dataItem);
                System.out.println("Timestamp: " + dataItem + " ms.");
                synchronized (buffer) {
                    buffer.notifyAll();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
