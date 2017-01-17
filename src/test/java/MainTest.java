import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class MainTest {

    @Test
    public void testMain() throws IOException {

        String[] args = {
//                "src/test/resources/test00.SWE",
//                "src/test/resources/test01.SWE",
//                "src/test/resources/test02.SWE",
//                "src/test/resources/test03.SWE",
//                "src/test/resources/test04.SWE",
//                "src/test/resources/test05.SWE",
                "src/test/resources/contest01.SWE",
//                "src/test/resources/test06.SWE",
//                "src/test/resources/test07.SWE",
        };

        Printer.preprocessor = false;
        Printer.info = false;
        // Printer.result = false;
        /*
        System.out.println("Warming up");
        for (int i = 0; i < 200; i++) {
            Main.main(new String[]{args[2]});
        }
        */

        Printer.result = true;

        for (String arg : args) {

            System.out.println("File: " + Paths.get(arg).getFileName().toString());
            Stopwatch watch = Stopwatch.createStarted();

            Main.main(new String[]{arg});

            watch.stop();

            System.out.println(watch.toString());
            System.out.println("");
        }

        //Main.main(args);
    }
}
