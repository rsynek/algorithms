import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private final int arraySize;
    private final WeightedQuickUnionUF connectedOpen;
    private final WeightedQuickUnionUF full;
    private final boolean [] openCells;
    private final int firstArtificialCell;
    private final int lastArtificialCell;

    /**
     * create n-by-n grid, with all sites blocked
     */
    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be positive integer.");
        }
        this.size = size;
        this.arraySize = size * size + 2;
        this.connectedOpen = new WeightedQuickUnionUF(arraySize);
        this.full = new WeightedQuickUnionUF(arraySize);
        this.openCells = new boolean[arraySize];
        this.firstArtificialCell = 0;
        this.lastArtificialCell = arraySize-1;
    }

    private int indexOf(int row, int col) {
        return (row - 1) * size + col;
    }

    private void checkRowAndCol(int row, int col) {
        checkCoordination(row);
        checkCoordination(col);
    }

    private void checkCoordination(int coordination) {
        if (coordination < 1 || coordination > size) {
            throw new IndexOutOfBoundsException(
                    String.format("The coordination %d is not between 1 and %d", coordination, size));
        }
    }

    /**
     * open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        checkRowAndCol(row, col);
        int index = indexOf(row, col);

        openCells[index] = true;

        connectArtificialCells(row, index);
        connectNeighbours(row, col, index);
    }

    private void connectArtificialCells(int row, int index) {
        if (row == 1) {
            connectedOpen.union(firstArtificialCell, index);
            full.union(firstArtificialCell, index);
        }

        if (row == size) {
            connectedOpen.union(index, lastArtificialCell);
        }
    }

    private void connectNeighbours(int row, int col, int index) {
        if (row > 1) {
            connectNeighbour(index, indexOf(row - 1, col));
        }

        if (row < size) {
            connectNeighbour(index, indexOf(row + 1, col));
        }

        if (col > 1) {
            connectNeighbour(index, indexOf(row, col - 1));
        }

        if (col < size) {
            connectNeighbour(index, indexOf(row, col + 1));
        }
    }

    private void connectNeighbour(int index, int neighbourIndex) {
        if (isOpen(neighbourIndex)) {
            connectedOpen.union(index, neighbourIndex);
            full.union(index, neighbourIndex);
        }
    }

    /**
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        checkRowAndCol(row, col);
        return isOpen(indexOf(row, col));
    }

    private boolean isOpen(int index) {
        return openCells[index];
    }

    /**
     * is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        checkRowAndCol(row, col);
        return isFull(indexOf(row, col));
    }

    private boolean isFull(int index) {
        return full.connected(firstArtificialCell, index);
    }

    public int numberOfOpenSites() {
        int numberOfOpen = 0;

        for (int i = 1; i <= size * size; i++) {
            if (openCells[i]) {
                numberOfOpen++;
            }
        }

        return numberOfOpen;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        return connectedOpen.connected(firstArtificialCell, lastArtificialCell);
    }

    public static void main(String[] args) {
        //InteractivePercolationVisualizer.main(args);
    }
}
