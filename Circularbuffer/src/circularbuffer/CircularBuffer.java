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
public class CircularBuffer<E> implements CircularBufferADT<E> {

    E[] buffer;
    int head = 0;
    int tail = 0;
    int length = 0;
    boolean isOverwritable = true;

    /**
     *
     * @param size Size of circular buffer
     */
    public CircularBuffer(int size) {
        buffer = (E[]) new Object[size];
    }

    /**
     *
     * @param size Size of circular buffer
     * @param canOverwrite Is the first element overwritable
     */
    public CircularBuffer(int size, boolean canOverwrite) {
        buffer = (E[]) new Object[size];
        isOverwritable = canOverwrite;
    }
    
    /**
     * default constructor of a circular buffer with 16 empty places
     */
    public CircularBuffer() {
        this(16);
    }

    @Override
    public synchronized boolean isEmpty() {
        return length == 0;
    }

    @Override
    public synchronized boolean isFull() {
        return length == buffer.length;
    }

    @Override
    public synchronized E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue exhausted");
        }
        E dequeued = buffer[tail];
        buffer[tail] = null;
        length--;
        tail = nextPosition(tail);
        return dequeued;
    }

    @Override
    public synchronized int nextPosition(int position) {
        return (position + 1) % buffer.length;
    }

    @Override
    public synchronized void push(E toAdd) {
        if(this.isOverwritable == false && length==buffer.length){
            //throw new IllegalStateException("Circular buffer is full");
            System.out.printf("Circular buffer is full, discarding value %s\n",toAdd);
            return;
        }
        buffer[head] = toAdd;
        head = nextPosition(head);
        if (isFull()) {
            tail = nextPosition(tail);
        } else {
            length++;
        }
    }

    /**
     * Returns the first element
     * @return The first element (first index)
     */
    public synchronized E peekFirst() {
        if (length == 0) {
            return null;
        } else {
            return buffer[tail];
        }
    }

    /**
     * Returns the last element
     * @return The last element (last index)
     */
    public synchronized E peekLast() {
        if (length == 0) {
            return null;
        } else if (head == 0) {
            return buffer[buffer.length - 1];
        } else {
            return buffer[head - 1];
        }
    }

    /**
     * Return the element at specified index
     * @param index the positions from which to retrieve the element
     * @return element in circular buffer at index
     */
    public synchronized E getElement(int index) {
        if (index >= 0 && index < buffer.length) {
            return buffer[(tail + index) % buffer.length];
        } else {
            return null;
        }
    }

    /**
     * Clears the circular buffer
     */
    @Override
    public void clear() {
        for (int i = 0; i < length; i++) {
            buffer[(tail + i) % buffer.length] = null;
        }
        head = 0;
        tail = 0;
        length = 0;
    }

    @Override
    public String toString() {
        int last;
        if (head == 0) {
            last = buffer.length - 1;
        } else {
            last = head - 1;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Content: ");
        for (int i = 0; i < buffer.length; i++) {
            int pos = nextPosition(i - 1);
            if(buffer[pos]!=null){
                if(pos == tail){
                    builder.append(">").append(buffer[nextPosition(i - 1)]).append(" ");    
                }else if(pos==last){
                    builder.append(buffer[nextPosition(i - 1)]).append("< "); 
                }
                else{
                    builder.append(buffer[nextPosition(i - 1)]).append(" ");
                }
            }
        }       
        builder.append("\nFirst index: ").append(tail).append(", last index: ").
                append(last).append(", pointer: ").append(head).append(", size: ").
                append(buffer.length).append(", has ").append(length).append(" elements.").
                append(" Legend: \">\" - Start, \"<\" - End");
        return builder.toString();
    }
    
    
}