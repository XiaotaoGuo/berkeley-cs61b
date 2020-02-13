public class LinkedListDeque<T> implements Deque<T> {

    private int size;
    private ListNode sentinel;

    /** ListNode class implementation*/
    public class ListNode {
        private ListNode prev;
        private ListNode next;
        private T item;

        /* constructor for one argument */
        public ListNode(T newItem) {
            item = newItem;
            prev = null;
            next = null;
        }

        /* default constructor */
        public ListNode() {
            item = null;
            prev = null;
            item = null;
        }
    }

    public LinkedListDeque() {
        sentinel = new ListNode();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T newItem) {
        ListNode newNode = new ListNode(newItem);

        newNode.next = sentinel.next;
        sentinel.next.prev = newNode;

        sentinel.next = newNode;
        newNode.prev = sentinel;

        size++;
    }

    @Override
    public void addLast(T newItems) {
        ListNode newNode = new ListNode(newItems);

        newNode.prev = sentinel.prev;
        sentinel.prev.next = newNode;

        sentinel.prev = newNode;
        newNode.next = sentinel;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        ListNode curr = sentinel.next;
        while (curr.next != sentinel) {
            System.out.print(curr.item + " ");
            curr = curr.next;
        }
        System.out.println(curr.item);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;

        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;

        return item;
    }

    @Override
    public T get(int index) {

        if (index >= size) {
            return null;
        }

        int currIdx = 0;
        ListNode curr = sentinel.next;
        while (currIdx != index) {
            curr = curr.next;
            currIdx++;
        }

        return curr.item;
    }

    public T getRecursive(int index) {

        if (index >= size) {
            return null;
        }

        return getHelper(index, sentinel.next);
    }

    private T getHelper(int index, ListNode curr) {
        if (index == 0) {
            return curr.item;
        }
        return getHelper(index - 1, curr.next);
    }

}
