package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
        }

        System.out.println("Start printing objects.");
        for (Object item : arb) {
            System.out.print(item + " ");
        }
        System.out.println();

        for (int i = 0; i < 4; i++) {
            arb.dequeue();
        }

        System.out.println("Start printing objects.");
        for (Object item : arb) {
            System.out.print(item + " ");
        }
        System.out.println();

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
