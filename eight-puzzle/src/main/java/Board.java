import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;

public class Board {

    private static final int BLANK = 0;

    private int[][] blocks;

    private int manhattanDistance;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.manhattanDistance = computeManhattanDistance();
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        int wrongValues = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != BLANK && blocks[i][j] != expectedValue(i, j)) {
                    wrongValues++;
                }
            }
        }

        return wrongValues;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != expectedValue(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private int expectedValue(int row, int col) {
        int dimension = dimension();
        return row == dimension - 1 && col == dimension - 1 ? BLANK : row * dimension + col + 1;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Random random = new Random();
        int dimension = dimension();

        int[][] twin = makeCopy();

        int i, j, k,l;
        do {
            i = random.nextInt(dimension);
            j = random.nextInt(dimension);
            k = random.nextInt(dimension);
            l = random.nextInt(dimension);
        }
        while ((i == k && j == l) || twin[i][j] == BLANK || twin[k][l] == BLANK);

        int temp = twin[i][j];
        twin[i][j] = twin[k][l];
        twin[k][l] = temp;

        return new Board(twin);
    }

    private int[][] makeCopy() {
        int dimension = dimension();
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = blocks[i][j];
            }
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        return Arrays.deepEquals(blocks, board.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int dimension = dimension();
        int zeroRow = -1, zeroCol = -1;

        //find zero
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == BLANK) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
            if (zeroRow > -1) {
                break;
            }
        }

        Collection<Board> neighbors = new Stack<>();
        if (zeroRow > 0) {
            neighbors.add(createNeighbor(zeroRow, zeroCol, zeroRow - 1, zeroCol));
        }

        if (zeroRow < dimension - 1) {
            neighbors.add(createNeighbor(zeroRow, zeroCol, zeroRow + 1, zeroCol));
        }

        if (zeroCol > 0) {
            neighbors.add(createNeighbor(zeroRow, zeroCol, zeroRow, zeroCol - 1));
        }

        if (zeroCol < dimension - 1) {
            neighbors.add(createNeighbor(zeroRow, zeroCol, zeroRow, zeroCol + 1));
        }

        return neighbors;
    }

    private Board createNeighbor(int zeroRow, int zeroCol, int neighborRow, int neighborCol) {
        int [][] neighbor = makeCopy();

        neighbor[zeroRow][zeroCol] = neighbor[neighborRow][neighborCol];
        neighbor[neighborRow][neighborCol] = 0;

        return new Board(neighbor);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append(System.lineSeparator());
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private int computeManhattanDistance() {
        int manhattan = 0;
        int dimension = dimension();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == BLANK) {
                    continue;
                }
                int expectedRow = (blocks[i][j] - 1) / dimension;
                int expectedCol = (blocks[i][j] - 1) % dimension;

                manhattan += Math.abs(expectedRow - i);
                manhattan += Math.abs(expectedCol - j);
            }
        }

        return manhattan;
    }
}