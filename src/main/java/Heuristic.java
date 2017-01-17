import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class Heuristic implements Comparator<Node> {


    public Heuristic() {
    }

    /**
     * Compare method
     *
     * @param n1
     * @param n2
     * @return
     */
    public int compare(Node n1, Node n2) {
        return f(n1) - f(n2);
    }

    /**
     * Heuristic function
     * Finds the distance from goal to the bo and from the agent to the box and combines them.
     *
     * @param n
     * @return
     */
    public int h(Node n) {

        Map.Entry<String, List<String>> entry = n.problem.getRwithLowestNumberOfExtensions();

        if (entry != null) {
            return entry.getValue().size();
        }

        //Not sure what to do in this case.
        return 0;
    }

    /**
     * Abstract function for f() to be implemented in extensions
     *
     * @param n
     * @return
     */
    public abstract int f(Node n);

    /**
     * A* implementation
     */
    public static class AStar extends Heuristic {
        public AStar() {
            super();
        }

        public int f(Node n) {
            return n.g() + h(n);
        }

        public String toString() {
            return "A* evaluation";
        }
    }

    /**
     * Weighted A* implementation
     */
    public static class WeightedAStar extends Heuristic {
        private int W;

        public WeightedAStar() {
            super();
            W = 5; // You're welcome to test this out with different values, but for the reporting part you must at least indicate benchmarks for W = 5
        }

        public int f(Node n) {
            return n.g() + W * h(n);
        }

        public String toString() {
            return String.format("WA*(%d) evaluation", W);
        }
    }

    /**
     * Greedy implementation
     */
    public static class Greedy extends Heuristic {

        public Greedy() {
            super();
        }

        public int f(Node n) {
            return h(n);
        }

        public String toString() {
            return "Greedy evaluation";
        }
    }
}
