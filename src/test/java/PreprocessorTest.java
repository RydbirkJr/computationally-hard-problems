import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Anders on 26/10/2016.
 */
public class PreprocessorTest {

    @Test
    public void testPreprocessor() throws IOException {

        String filename = "src/test/resources/test03.SWE";

        List<String> lines = FileReader.readFile(filename);

        Problem problem = new Decoder().decode(lines);
        Stopwatch watch = Stopwatch.createStarted();
        new Preprocessor().preprocess(problem, true);
        watch.stop();
        System.out.println(watch.toString());
    }
}
