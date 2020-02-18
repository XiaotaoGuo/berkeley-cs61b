package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;

    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % capacity;
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = rb[first];
        first = (first + 1) % capacity;
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    private class BufferIterator<T> implements Iterator<T> {
        private int ptr;
        private boolean beginIterate;

        BufferIterator() {
            ptr = first;
            beginIterate = false;
        }

        @Override
        public boolean hasNext() {
            if (!beginIterate) {
                return !isEmpty();
            }
            return ptr != last;
        }

        @Override
        public T next() {
            beginIterate = true;
            T item = (T) rb[ptr];
            ptr = (ptr + 1) % capacity;
            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator<>();
    }
}
