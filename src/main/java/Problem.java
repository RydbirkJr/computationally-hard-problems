import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Problem {

    private HashMap<String, List<String>> originalR;
    private String s;
    private List<String> t;
    private HashMap<String, List<String>> R;
    private HashMap<String, List<Problem>> futureProblems;
    private static final String REPLACER = "[\\\\w]+";
    private static final String ADDON = "[\\w]*";
    private final boolean isValid;
    public final String key;
    public final String extension;
    public int totalValidTs = 0;

    public Problem(String s, List<String> t, HashMap<String, List<String>> r) {
        this.s = s;
        this.t = t;
        this.originalR = r;
        this.R = r;
        this.isValid = true;
        this.key = "Initial state";
        this.extension = "Initial state";
    }

    public Problem(Problem prev, String key, String extension) {
        //Copy old data structures
        this.s = prev.getS();
        this.originalR = new HashMap<>(prev.originalR);
        this.R = new HashMap<>(prev.getR());
        this.key = key;
        this.extension = extension;

        List<String> notEditedTs = prev.getT().stream().filter(str -> !str.contains(key)).collect(Collectors.toList());
        List<String> editedTs = prev.getT().stream()
                .filter(str -> str.contains(key))
                .map(tString -> tString.replaceAll(key, extension))
                .collect(Collectors.toList());

        notEditedTs.addAll(editedTs);

        //Replace key in the string with the given extension
        this.t = notEditedTs;

        //Store result and remove key from R
        this.R.remove(key);
        this.originalR.remove(key);

        this.isValid = this.validate(editedTs);

        if(Search.TYPE == SearchType.Optimize){
            this.totalValidTs = validateAll(this.t);
        }
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public List<String> getT() {
        return t;
    }

    public void setT(List<String> t) {
        this.t = t;
    }

    public HashMap<String, List<String>> getR() {
        return R;
    }

    public void setR(HashMap<String, List<String>> r) {
        this.R = r;
    }

    public boolean isValid() {
        return this.isValid;
    }

    private int validateAll(List<String> Ts){
        //total number of valid strings
        return (int) Ts.stream()
                .map(ent -> {
                    String pat = ent.replaceAll("[A-Z]", REPLACER);
                    //Append a star if not a plus
                    if (pat.charAt(pat.length() - 1) != '+') {
                        pat += ADDON;
                    }

                    //Prepend a prefix in case there is a lowercase letter first
                    if (pat.charAt(0) != '[') {
                        pat = ADDON + pat;
                    }

                    return Pattern.compile(pat);
                })
                .filter(mat -> mat.matcher(s).matches())
                .count();
    }

    private boolean validate(List<String> editedTs) {
        //replace all uppercase letters and matches against s

        boolean allTsAreValid = editedTs.stream()
                .map(ent -> {
                    String pat = ent.replaceAll("[A-Z]", REPLACER);
                    //Append a star if not a plus
                    if (pat.charAt(pat.length() - 1) != '+') {
                        pat += ADDON;
                    }

                    //Prepend a prefix in case there is a lowercase letter first
                    if (pat.charAt(0) != '[') {
                        pat = ADDON + pat;
                    }

                    return Pattern.compile(pat);
                })
                .allMatch(mat -> {
                    boolean match = mat.matcher(s).matches();
                    //System.out.println("Match for pattern " + mat.pattern() + " : " + match);
                    return match;
                });

        boolean allRsAreValid = this.R.values()
                .stream()
                .allMatch(extensionSet -> !extensionSet.isEmpty());

        return allTsAreValid && allRsAreValid;
        //.map(pat -> new Tuple(pat, pat.matcher(this.s)))
        //.collect(Collectors.toList());

//        list.forEach(tup -> System.out.println("Pattern: " + tup.pat.pattern() + ", match: " + tup.match.matches()));
    }

    public void pruneProblem() {
        this.R = pruneRForInvalidExtensions();
    }

    /**
     * Bound by O(|R|*|T|)
     */
    private HashMap<String, List<String>> pruneRForInvalidExtensions() {
        HashMap<String, List<String>> prunedR = new HashMap<>(this.R.size());
        this.futureProblems = new HashMap<>(this.R.size());

        HashMap<String, List<String>> Rset = this.R;

        //Overwrite selected R in case of optimization - then use ALL valid R expansions
        if(Search.TYPE == SearchType.Optimize){
            Rset = this.originalR;
        }

        Rset.entrySet()
                .forEach(entry -> {
                    //Get R and insert new key in pruned array
                    String R = entry.getKey();
                    prunedR.put(R, new ArrayList<>());
                    this.futureProblems.put(R, new ArrayList<>(entry.getValue().size()));

                    entry.getValue()
                            .forEach(extension -> {
                                Problem temp = new Problem(this, R, extension);

                                if (temp.isValid()) {
                                    //Store future problem - if we decide to continue work on this one, no need to process it again
                                    this.futureProblems.get(R).add(temp);
                                    prunedR.get(R).add(extension);
                                } else if (Search.TYPE == SearchType.Optimize) {
                                    this.futureProblems.get(R).add(temp);
                                }

                            });
                });

//        prunedR.forEach((key, extensionSet) -> {
//            System.out.println(key + ":");
//            extensionSet.forEach(extension -> System.out.println("\t" + extension));
//        });

        return prunedR;
    }

    public List<Problem> getFutureProblems(String key) {
        return this.futureProblems.get(key);
    }

    public Map.Entry<String, List<String>> getRwithLowestNumberOfExtensions() {


        Optional<Map.Entry<String, List<String>>> min = this.R.entrySet()
                .stream()
                .min((entry1, entry2) -> entry1.getValue().size() - entry2.getValue().size());

        if (min.isPresent()) {
            return min.get();
        }

        //What to do if no R left?
        return null;
    }
}