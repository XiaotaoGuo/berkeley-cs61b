package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Solver {
    private List<WorldState> sol;
    private Node finish;

    public Solver(WorldState initial) {
        MinPQ<Node> pq = new MinPQ<>();
        Node start = new Node(0, initial, null);
        pq.insert(start);
        Set<WorldState> visited = new HashSet<>();
        Node curr = null;
        while (!pq.isEmpty()) {
            curr = pq.delMin();
            visited.add(curr.state);
            if (curr.state.isGoal()) {
                finish = curr;
                break;
            }
            for (WorldState next : curr.state.neighbors()) {
                if (visited.contains(next)) {
                    continue;
                }
                Node n = new Node(curr.moves + 1, next, curr);
                pq.insert(n);
            }
        }

        sol = new ArrayList<>();
        while (curr != null) {
            sol.add(curr.state);
            curr = curr.prev;
        }

        Collections.reverse(sol);

    }

    private class Node implements Comparable {
        private int moves;
        private int distance;
        private WorldState state;
        private Node prev;

        Node(int moves, WorldState state, Node prev) {
            this.distance = state.estimatedDistanceToGoal();
            this.moves = moves;
            this.prev = prev;
            this.state = state;
        }


        @Override
        public int compareTo(Object o) {
            if (o.getClass() == this.getClass()) {
                Node other = (Node) o;
                return (this.moves + this.distance) - (other.moves + other.distance);
            }

            return -1;
        }
    }

    public int moves() {
        return sol.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return sol;
    }
}
