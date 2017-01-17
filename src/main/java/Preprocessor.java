import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anders on 26/10/2016.
 */
public class Preprocessor {

    public Problem preprocess(Problem problem, boolean pruneT) {
        String s = problem.getS();

        //Prune R for invalid extensions
        HashMap<String, List<String>> R = pruneR(problem);

        //Prune T for subsets of other sets
        List<String> T = problem.getT();
        if(pruneT) {
            T = pruneT(problem);
        }

        Problem preprocessed = new Problem(s, T, R);

        return preprocessed;
    }

    /**
     * Bound by O(|R|*s)
     * @param problem
     * @return
     */
    private HashMap<String, List<String>> pruneR(Problem problem) {

        HashSet<Character> RSet = new HashSet<>();

        problem.getT()
                .forEach(t -> {
                    char[] temp = t.toCharArray();
                    for (int i = 0; i < temp.length; i++) {
                        if (Character.isUpperCase(temp[i])) {
                            RSet.add(temp[i]);
                        }
                    }
                });

        Printer.preprocessor("Allowed Rs in problem:");
        RSet.forEach(r -> Printer.preprocessor(r + ""));

        HashMap<String, List<String>> newR = new HashMap<>();

        for (Map.Entry<String, List<String>> R : problem.getR().entrySet()) {

            //Prunes not needed R values
            if (!RSet.contains(R.getKey().charAt(0))) {
                Printer.preprocessor("Pruned R: " + R.getKey());
                continue;
            }
            List<String> newRValues = R.getValue().stream().filter(r -> {
                return problem.getS().contains(r);
            }).collect(Collectors.toList());

            newR.put(R.getKey(), newRValues);
        }

        Printer.preprocessor("");
        return newR;

    }

    /**
     * Bound by O(|T|^2 + |T|) = O(|T|^2)
     * @param problem
     * @return
     */
    private List<String> pruneT(Problem problem) {
        List<String> list = new ArrayList<>(problem.getT());
        Collections.sort(list, (o1, o2) -> o2.length() - o1.length());

        List<String> prunedT = new ArrayList<>(list.size());

        list.forEach(t -> {
            //Skip if it only contains lowercase letters
            if (t.matches("^[a-z]+$")) return;

            //If prunedT contains sorted OR prunedT contains a superset of sorted, do not insert
            if (!(prunedT.contains(t) || prunedT.stream().anyMatch(pruned -> pruned.contains(t)))) {
                prunedT.add(t);
            } else {
                //prints the pruned T
                Printer.preprocessor("Pruned T: " + t);
            }
        });

        Printer.preprocessor("");
        return list;
    }

}
