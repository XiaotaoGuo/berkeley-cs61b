import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> singleQueue = new Queue<>();
        for (Item item : items) {
            Queue<Item> nq = new Queue<>();
            nq.enqueue(item);
            singleQueue.enqueue(nq);
        }
        return singleQueue;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> sortedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            sortedQueue.enqueue(getMin(q1, q2));
        }
        return sortedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.isEmpty() || items.size() == 1) {
            return items;
        }

        Queue<Queue<Item>> qitems = makeSingleItemQueues(items);
        while (qitems.size() > 1) {
            Queue<Queue<Item>> nqitems = new Queue<>();
            while (qitems.size() > 1) {
                Queue<Item> first = qitems.dequeue();
                Queue<Item> second = qitems.dequeue();
                nqitems.enqueue(mergeSortedQueues(first, second));
            }
            if (qitems.size() == 1) {
                nqitems.enqueue(qitems.dequeue());
            }
            qitems = nqitems;
        }


        return qitems.dequeue();
    }

    public static void main(String[] args) {
        Queue<Integer> nums = new Queue<>();
        nums.enqueue(5);
        nums.enqueue(4);
        nums.enqueue(3);
        nums.enqueue(2);
        nums.enqueue(1);
        Queue<Integer> sortedQueue = MergeSort.mergeSort(nums);
        System.out.println("Unsorted queue: ");
        for (Integer num: nums) {
            System.out.print(num + " ");
        }
        System.out.println(" ");

        System.out.println("Sorted queue: ");
        for (Integer num: sortedQueue) {
            System.out.print(num + " ");
        }
        System.out.println(" ");

    }
}
