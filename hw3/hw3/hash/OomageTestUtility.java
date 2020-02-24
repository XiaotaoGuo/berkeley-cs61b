package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        
        int N = oomages.size();
        int[] bucket = new int[M];
        for (int i = 0; i < M; i++) {
            bucket[i] = 0;
        }

        for (Oomage o : oomages) {
            int index = (o.hashCode() & 0x7FFFFFFF) % M;
            bucket[index]++;
        }

        for (int i = 0; i < M; i++) {
            if (bucket[i] < N / 50 || bucket[i] > N / 2.5) {
                return false;
            }
        }

        return true;
    }
}
