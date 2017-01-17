import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anders on 26/10/2016.
 */
public class FileReader {
    public static List<String> readFile(String filename) throws IOException {
        return (List<String>) Files.lines(
                FileSystems.getDefault().getPath(filename)
        ).collect(Collectors.toList());
    }
}
