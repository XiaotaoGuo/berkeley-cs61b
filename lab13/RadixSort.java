/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int width = 0;
        for (String str: asciis) {
            width = width < str.length() ? str.length() : width;
        }

        String[] copy = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            copy[i] = asciis[i];
        }

        for (int i = 0; i < width; i++) {
            sortHelperLSD(copy, i);
        }

        return copy;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] buckets = new int[256];
        int[] starts = new int[256];

        for (int i = 0; i < 256; i++) {
            buckets[i] = 0;
        }

        for (String str: asciis) {
            int place;
            if (index >= str.length()) {
                place = 0;
            } else {
                place = (int) str.charAt(str.length() - 1 - index);
            }

            buckets[place]++;
        }

        starts[0] = 0;
        for (int i = 1; i < 256; i++) {
            starts[i] = starts[i - 1] + buckets[i - 1];
        }

        String[] sorted = new String[asciis.length];
        for (String str: asciis) {
            int place;
            if (index >= str.length()) {
                place = 0;
            } else {
                place = (int) str.charAt(str.length() - 1 - index);
            }
            int pos = starts[place];
            sorted[pos] = str;
            starts[place]++;
        }

        asciis = sorted;
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
