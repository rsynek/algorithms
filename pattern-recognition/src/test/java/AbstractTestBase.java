import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public abstract class AbstractTestBase {

    private static Path DATA_DIR = Paths.get("data");

    protected Point[] readInputFile(String inputFileName) {
        try (Scanner scanner = new Scanner(DATA_DIR.resolve(inputFileName))) {
            int numberOfLines = scanner.nextInt();
            scanner.nextLine();
            Point [] points = new Point[numberOfLines];
            for (int i = 0; i < numberOfLines; i++) {
                String line = scanner.nextLine().trim();
                String[] tokens = line.split(" +");
                if (tokens.length == 2) {
                    try {
                        int x = Integer.parseInt(tokens[0].trim());
                        int y = Integer.parseInt(tokens[1].trim());
                        points[i] = new Point(x, y);
                    } catch (NumberFormatException ex) {
                        points[i] = null;
                    }
                } else {
                    points[i] = null;
                }
            }

            return points;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
