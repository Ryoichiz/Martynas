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
public interface CircularBufferADT<E> {

    /**
     * Check if circular buffer is empty
     * @return is the circular buffer empty
     */
    public boolean isEmpty();

    /**
     * Check if circular buffer is full
     * @return is the circular buffer full
     */
    public boolean isFull();

    /**
     * Removes the element at tail 
     * @return The removed element
     */
    public E pop();

    /**
     * Returns the next position in circular buffer
     * @param position current position
     * @return The next available index
     */
    public int nextPosition(int position);

    /**
     * Add an element to the circular buffer's head
     * @param toAdd Element to be added
     */
    public void push(E toAdd);
    
    /**
     * Clears the circular buffer
     */
    public void clear();

}