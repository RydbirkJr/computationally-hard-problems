import java.util.*;

public class Node {

    private static Random rnd = new Random(1);
    public static Node bestResult;

    public Node parent;
    public String key;
    public String extension;

    private int g;
    public final Problem problem;

    public Node(Node parent, Problem problem) {

        this.problem = problem;
        this.parent = parent;

        this.problem.pruneProblem();

        if (parent == null) {
            g = 0;
            bestResult = this;
            //Other init options?

        } else {
            this.key = problem.key;
            this.extension = problem.extension;
            g = parent.g() + 1;

            //Store this result if better
            if(bestResult.g() < this.g){
                bestResult = this;
            }
        }
    }

    public int g() {
        return g;
    }

    public boolean isInitialState() {
        return this.parent == null;
    }

    public boolean isGoalState() {
        if(Search.TYPE == SearchType.Decision){
            return problem.getR().isEmpty() && problem.isValid();
        } else {
            return problem.getR().isEmpty();
        }

    }

    public ArrayList<Node> getExpandedNodes() {


        ArrayList<Node> expandedNodes = new ArrayList<>();

        Map.Entry<String, List<String>> entry = this.problem.getRwithLowestNumberOfExtensions();

        if (entry == null) return expandedNodes;

        if(Search.TYPE == SearchType.Decision){
            this.problem.getFutureProblems(entry.getKey())
                    .forEach(prob -> expandedNodes.add(new Node(this, prob)));

            //Fine with a little shuffle maybe - not sure.
            Collections.shuffle(expandedNodes, rnd);
        } else {
            // optimization problem - should always yield a result.. I think
            this.problem.getFutureProblems(entry.getKey())
                    .stream()
                    .max((p1, p2) -> p1.totalValidTs - p2.totalValidTs)
                    .ifPresent(p -> expandedNodes.add(new Node(this, p)));
        }

        return expandedNodes;
    }

    public HashMap<String, String> getResult() {
        HashMap<String, String> result = new HashMap<>();

        Node n = this;
        while (!n.isInitialState()) {
            result.put(n.key, n.extension);
            n = n.parent;
        }
        return result;
    }

    public LinkedList<Node> extractPlan() {
        LinkedList<Node> plan = new LinkedList<Node>();
        Node n = this;
        while (!n.isInitialState()) {
            plan.addFirst(n);
            n = n.parent;
        }
        return plan;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        this.extractPlan()
                .forEach(node -> s.append(node.key + " : " + node.extension + "\n"));

        return s.toString();
    }

}