/**
 * Created by Anders on 26/10/2016.
 */
public class Printer {

    public static boolean info = false;
    public static boolean preprocessor = false;
    public static boolean result = true;

    public static void info(String string) {
        if (info) System.out.println(string);
    }

    public static void preprocessor(String string) {
        if (preprocessor) System.out.println(string);
    }

    public static void result(String string) {
        if (result) System.out.println(string);
    }
}
