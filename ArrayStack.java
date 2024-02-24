public class ArrayStack<T> implements StackADT<T> {
    private T[] data; // Array to store stack elements
    private int topIndex; // Index of the top element

    public ArrayStack() {
        // Initialize an empty stack with an initial capacity of 10
        data = (T[]) new Object[10];
        topIndex = -1; // Initialize topIndex to -1 indicating stack is empty
    }

    @Override
    public void push(T element) {
        // Add an element to the top of the stack
        // If stack is 75% full, expand its capacity to accommodate more elements
        if (size() >= 0.75 * data.length) {
            expandCapacity();
        }
        topIndex++; // Increment topIndex
        data[topIndex] = element; // Store the element at the new top
    }

    @Override
    public T pop() throws StackException {
        // Remove and return the top element of the stack
        // Throws an exception if the stack is empty
        if (isEmpty()) {
            throw new StackException("Stack is empty");
        }

        // Shrink the stack's capacity if it is less than 25% full and the capacity is greater than 20
        if (data.length >= 20 && size() <= 0.25 * data.length) {
            shrinkCapacity();
        }

        T element = data[topIndex]; // Get the top element
        data[topIndex] = null; // Clear the top element
        topIndex--; // Decrement topIndex

        return element; // Return the removed element
    }

    @Override
    public T peek() throws StackException {
        // Return the top element without removing it
        // Throws an exception if the stack is empty
        if (isEmpty()) {
            throw new StackException("Stack is empty");
        }
        return data[topIndex]; // Return the top element
    }

    @Override
    public boolean isEmpty() {
        // Check if the stack is empty
        return topIndex == -1;
    }

    @Override
    public int size() {
        // Return the number of elements in the stack
        return topIndex + 1;
    }

    @Override
    public void clear() {
        // Clear the stack by setting all elements to null and resetting the array's capacity to 10
        for (int i = 0; i <= topIndex; i++) {
            data[i] = null;
        }
        topIndex = -1;
        data = (T[]) new Object[10];
    }

    public int getCapacity() {
        // Return the current capacity of the stack
        return data.length;
    }

    public int getTop() {
        // Return the index of the top element in the stack
        return topIndex;
    }

    @Override
    public String toString() {
        // Generate a string representation of the stack
        if (isEmpty()) {
            return "Empty stack.";
        }

        StringBuilder str = new StringBuilder("Stack: ");
        for (int i = topIndex; i >= 0; i--) {
            str.append(data[i]);
            if (i > 0) {
                str.append(", ");
            } else {
                str.append(".");
            }
        }
        return str.toString();
    }

    private void expandCapacity() {
        // Expand the capacity of the array to accommodate more elements
        int newCapacity = getCapacity() + 10; // Increase capacity by 10
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(data, 0, newArray, 0, getCapacity()); // Copy elements to new array
        data = newArray;
    }

    private void shrinkCapacity() {
        // Reduce the capacity of the array when it is underutilized
        int newCapacity = getCapacity() - 10; // Decrease capacity by 10
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(data, 0, newArray, 0, newCapacity); // Copy elements to the new smaller array
        data = newArray;
    }
}
