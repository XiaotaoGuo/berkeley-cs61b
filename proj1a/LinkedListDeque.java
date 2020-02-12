public class LinkedListDeque <Typename> {

    private int size;
    private ListNode sentinel;

    /** ListNode class implementation*/
    public class ListNode {
        public ListNode prev;
        public ListNode next;
        public Typename item;

        /* constructor for one argument */
        public ListNode(Typename item_) {
            item = item_;
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

    public void addFirst(Typename item_) {
        ListNode newNode = new ListNode(item_);

        newNode.next = sentinel.next;
        sentinel.next.prev = newNode;

        sentinel.next = newNode;
        newNode.prev = sentinel;

        size++;
    }

    public void addLast(Typename item_) {
        ListNode newNode = new ListNode(item_);

        newNode.prev = sentinel.prev;
        sentinel.prev.next = newNode;

        sentinel.prev = newNode;
        newNode.next = sentinel;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ListNode curr = sentinel.next;
        while (curr.next != sentinel) {
            System.out.print( curr.item + " ");
            curr = curr.next;
        }
        System.out.println(curr.item);
    }

    public Typename removeFirst() {
        if (sentinel.next == null) {
            return null;
        }

        Typename item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;

        return item;
    }

    public Typename removeLast() {
        if (sentinel.prev == null) {
            return null;
        }

        Typename item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;

        return item;
    }

    public Typename get(int index) {

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

    public Typename getRecursive(int index) {

        if (index >= size) {
            return null;
        }

        return getHelper(index, sentinel.next);
    }

    private Typename getHelper(int index, ListNode curr) {
        if (index == 0) {
            return curr.item;
        }
        return getHelper(index - 1, curr.next);
    }

}
