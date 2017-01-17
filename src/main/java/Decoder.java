import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Decoder {

    public Problem decode(List<String> contents) {
        HashSet<Character> gammaLetters = new HashSet<>();

        // remove empty strings
        contents = contents.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

        if (contents.size() < 4) {
            Printer.result("Not enough lines in file to contain k, s, t and R.");
            System.exit(1);
        }

        try {
            int k = Integer.parseInt(contents.get(0));

            String s = contents.get(1);

            if (!s.matches("^[a-z]+$")) {
                Printer.result("Not all characters in s (" + s + ") are in the alphabet.");
                System.exit(1);
            }

            List<String> t = new ArrayList<>();

            for (int i = 2; i < k + 2; i++) {
                String t_i = contents.get(i);

                if (!t_i.matches("^[a-zA-Z]+$")) {
                    Printer.result("String t_" + i + " is not valid.");
                    System.exit(1);
                }

                t.add(t_i);

                for (char c : t_i.toCharArray()) {
                    if (Character.isUpperCase(c)) gammaLetters.add(c);
                }
            }

            HashMap<String, List<String>> map = new HashMap<>();

            //validate R values
            for (int i = 2 + k; i < contents.size(); i++) {
                Pattern pattern = Pattern.compile("^([A-Z]{1}):([a-z,]+[a-z]+)$");
                Matcher matcher = pattern.matcher(contents.get(i));

                if (matcher.matches()) {
                    String R = matcher.group(1);
                    List<String> extensions = Arrays.asList(matcher.group(2).split(","));

                    if (gammaLetters.contains(R.charAt(0))) {
                        gammaLetters.remove(R.charAt(0));
                    }

                    map.put(R, new ArrayList<>(extensions));

                } else {
                    Printer.result("Did not found a matching pattern for R.");
                    System.exit(0);
                }
            }

            if (!gammaLetters.isEmpty()) {
                Printer.result("Did not find extension sets for all letters in Gamma.");
                System.exit(1);
            }

            return new Problem(s, t, map);

        } catch (NumberFormatException e) {
            Printer.result("Wrong format for k.");
        } catch (IndexOutOfBoundsException e) {
            Printer.result("Not enough files.");
        }

        //Should never be reached.
        return null;
    }
}
