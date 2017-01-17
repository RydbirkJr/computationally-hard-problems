public class Solver {

    private final Problem initialProblem;

    public Solver(Problem problem) {
        this.initialProblem = problem;
    }

    /**
     * Bound by O((b^d * R * |R| * |T|) + |R|*s + |T|^2)
     *      where b is the branching factor and d is the depth
     *      b = size of expansions in R
     *      d = |T|
     * @return
     */
    public Node solve() {

        Problem preprocessed;
        SearchClient sc;
        Node n;

//         preprocessed = new Preprocessor().preprocess(initialProblem, true);
//         sc = new SearchClient(preprocessed);
//
//        n = sc.Search(new Strategy.StrategyBestFirst(new Heuristic.WeightedAStar()));
//
//        if(n != null){
//            return n;
//        }

        //Continue search - no complete solution exists

        System.out.println("switched to DFS");

        //Do not prune the initial problem for T.
        Search.TYPE = SearchType.Optimize;
        preprocessed = new Preprocessor().preprocess(initialProblem, false);
        System.out.println("Preprocessed");
        sc = new SearchClient(preprocessed);
        n = sc.Search(new Strategy.StrategyDFS());

//        System.out.println("Found solution for DFS with " + n.problem.totalValidTs + " valid strings out of " + preprocessed.getT().size() + " strings in T.");

        return n;
    }
}
