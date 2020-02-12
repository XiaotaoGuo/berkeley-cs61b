public class ArrayDeque<T> {
    private int size;
    private int frontPtr;
    private int backPtr;
    private int maxSize;
    private final double ratio = 0.04;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        frontPtr = 0;
        backPtr = 0;
        maxSize = 8;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }

        if (isEmpty()) {
            items[frontPtr] = item;
            backPtr++;
            backPtr %= maxSize;
        } else {
            frontPtr = frontPtr == 0 ? maxSize - 1 : frontPtr - 1;
            items[frontPtr] = item;
        }

        size++;
    }

    public void addLast(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }

        items[backPtr] = item;
        backPtr++;
        backPtr %= maxSize;
        size++;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T item = items[frontPtr];
        frontPtr++;
        frontPtr %= maxSize;
        size--;

        if ((double) size / maxSize <= 0.25 && size > 8) {
            resize((int) (size * 0.5));
        }

        return item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        backPtr = backPtr == 0 ? maxSize - 1 : backPtr - 1;
        T item = items[backPtr];
        size--;

        if ((double) size / maxSize <= 0.25 && size > 8) {
            resize((int) (size * 0.5));
        }

        return item;

    }

    public void printDeque() {
        int idx = 0;
        int currPtr = frontPtr;
        while (idx < size) {
            System.out.print(items[currPtr] + " ");
            idx++;
            currPtr = (currPtr + 1) % maxSize;
        }
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }

        int currPtr = (frontPtr + index) % maxSize;
        return items[currPtr];
    }

    /* Utility function to copy data from old array to new array */
    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int currPtr = frontPtr;
        int idx = 0;
        while (idx < size) {
            newItems[idx] = items[currPtr];
            idx++;
            currPtr = (currPtr + 1) % maxSize;
        }
        items = newItems;
        maxSize = newSize;
        frontPtr = 0;
        backPtr = size;

    }
}
