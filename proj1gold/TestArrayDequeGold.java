import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> ansDeq = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solDeq = new ArrayDequeSolution<>();
        String message = "";
        while (true) {
            int choice;
            if (solDeq.isEmpty() || ansDeq.isEmpty()) {
                choice = StdRandom.uniform(5);
            } else {
                choice = StdRandom.uniform(7);
            }
            if (choice == 0) {
                message += "size()\n";
                assertEquals(message, solDeq.size(), ansDeq.size());
            } else if (choice == 1) {
                message += "isEmpty()\n";
                assertEquals(message, solDeq.isEmpty(), ansDeq.isEmpty());
            } else if (choice == 2) {
                Integer newItem = StdRandom.uniform(100);
                message += "addFirst(" + newItem + ")\n";
                solDeq.addFirst(newItem);
                ansDeq.addFirst(newItem);
            } else if (choice == 4) {
                Integer newItem = StdRandom.uniform(100);
                message += "addLast(" + newItem + ")\n";
                solDeq.addLast(newItem);
                ansDeq.addLast(newItem);
            } else if (choice == 5) {
                message += "removeFirst()\n";
                assertEquals(message, solDeq.removeFirst(), ansDeq.removeFirst());
            } else if (choice == 6) {
                message += "removeLast()\n";
                assertEquals(message, solDeq.removeLast(), ansDeq.removeLast());
            }
        }

    }
}
