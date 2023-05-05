import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SolverTest {

    private static Path DATA_DIR = Paths.get("data");

    @Test
    public void debugging() {
        Solver solver = new Solver(readInputFile("puzzle4x4-02.txt"));

        boolean isSolvable = solver.isSolvable();
        System.out.println(String.format("Is solvable: %b in %d moves", isSolvable, solver.moves()));

        if (isSolvable) {
            solver.solution().forEach(board -> System.out.println(board.toString()));
        }
    }

    protected Board readInputFile(String inputFileName) {
        try (Scanner scanner = new Scanner(DATA_DIR.resolve(inputFileName))) {
            int size = scanner.nextInt();
            int [][] blocks = new int[size][size];
            scanner.nextLine();

            for (int i = 0; i < size; i++) {
                String line = scanner.nextLine().trim();
                String[] tokens = line.split(" +");
                for (int j = 0; j < tokens.length; j++) {
                    blocks[i][j] = Integer.parseInt(tokens[j].trim());
                }
            }

            return new Board(blocks);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
