package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Solver {
    private List<WorldState> sol;

    public Solver(WorldState initial) {
        MinPQ<WorldState> pq = new MinPQ<>();
        pq.insert(initial);
        Map<WorldState, WorldState> prev = new HashMap<>();
        Set<WorldState> visited = new HashSet<>();
        prev.put(initial, null);
        WorldState curr = null;
        while (!pq.isEmpty()) {
            curr = pq.min();
            pq.delMin();
            visited.add(curr);
            if (curr.isGoal()) {
                break;
            }
            for (WorldState next : curr.neighbors()) {
                if (visited.contains(next)) {
                    continue;
                }
                prev.put(next, curr);
                pq.insert(next);
            }
        }

        sol = new ArrayList<>();
        while (curr != null) {
            sol.add(curr);
            curr = prev.get(curr);
        }

        Collections.reverse(sol);



    }

    public int moves() {
        return sol.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return sol;
    }
}
