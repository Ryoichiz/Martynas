/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

/**
 *
 * @author Martynas
 */
public class CircularBufferTesting {
    
    public static void Bandymai() {
        CircularBuffer<Integer> buffer = new CircularBuffer<>(5);
        buffer.push(1);
        buffer.push(2);
        buffer.push(3);
        buffer.push(4);
        buffer.push(5);

        System.out.println(buffer.toString());

        while (!buffer.isEmpty()) {
            System.out.printf("Dequeued %d\n", buffer.pop());
        }

        System.out.println("\n");
        CircularBuffer<Integer> buffer2 = new CircularBuffer<>(5,false);
        buffer2.push(1);
        buffer2.push(2);
        buffer2.push(3);
        buffer2.push(4);
        buffer2.push(5);
        buffer2.push(6);

        System.out.println(buffer2.toString());

        System.out.printf("Get the %d element %d\n", 0, buffer2.getElement(0));
        
        while (!buffer2.isEmpty()) {
            System.out.printf("Dequeued %d\n", buffer2.pop());
        }

    }

    

    public static void main(String[] args) {
        Bandymai();
    }
}
