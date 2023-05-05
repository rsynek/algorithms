import edu.princeton.cs.algs4.Picture;
import java.lang.Math;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private static final double BORDER_ENERGY = 1000.0;

    private Picture picture;

    /**
     * Create a seam carver object based on the given picture.
     * */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture cannot be null.");
        }
        this.picture = picture;
    }

    /**
     * Current picture.
     * */
    public Picture picture() {
        return this.picture;
    }

    /**
     * Width of current picture.
     * */
    public int width() {
        return this.picture.width();
    }

    /**
     * Height of current picture.
     */
    public int height() {
        return this.picture.height();
    }

    /**
     * Energy of pixel at column x and row y.
     * */
    public double energy(int x, int y) {
        validatePixel(x, y);

        if (isBorder(x, y)) {
            return BORDER_ENERGY;
        }

        Color leftNeighbour = picture().get(x - 1, y);
        Color rightNeighbour = picture().get(x + 1, y);
        Color upperNeighbour = picture().get(x, y - 1);
        Color lowerNeighbour = picture().get(x, y + 1);

        return Math.sqrt(grad(leftNeighbour, rightNeighbour) + grad(upperNeighbour, lowerNeighbour));
    }

    private void validatePixel(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException(String.format("Pixel coordinates [%d, %d] out of bounds.", x, y));
        }
    }

    private boolean isBorder(int x, int y) {
        boolean xOnBorder = x == 0 || x == width() - 1;
        boolean yOnBorder = y == 0 || y == height() - 1;

        return xOnBorder || yOnBorder;
    }

    private double grad(Color a, Color b) {
        double deltaR = secondPow(a.getRed() - b.getRed());
        double deltaG = secondPow(a.getGreen() - b.getGreen());
        double deltaB = secondPow(a.getBlue() - b.getBlue());

        return deltaR + deltaG + deltaB;
    }

    private double secondPow(double number) {
        return Math.pow(number, 2);
    }

    /**
     * Sequence of indices for horizontal seam.
     * */
    public int[] findHorizontalSeam() {
        this.picture = transpose();
        int [] verticalSeam = findVerticalSeam();
        this.picture = transpose();

        return verticalSeam;
    }

    /**
     * Sequence of indices for vertical seam.
     * */
    public int[] findVerticalSeam() {
        double [][] energies = computeEnergies();

        // init distances with first row
        double [] distancesPreviousRow = Arrays.copyOf(energies[0], width());
        double [] distancesCurrentRow = new double[width()];

        int [][] paths = new int[height()-1][width()];

        for (int row = 1; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                double minValue = Double.POSITIVE_INFINITY;
                int minEdge = col;
                for (int edge : edges(col)) {
                     double temp = distancesPreviousRow[edge] + energies[row][col];
                     if (temp < minValue) {
                         minValue = temp;
                         minEdge = edge;
                     }
                }
                distancesCurrentRow[col] = minValue;
                paths[row - 1][col] = minEdge;
            }
            distancesPreviousRow = Arrays.copyOf(distancesCurrentRow, width());
        }

        //process the current row distances, as there are the shortest paths ends
        double minValue = Double.POSITIVE_INFINITY;
        int seamIndex = 0;
        for (int i = 0; i < distancesCurrentRow.length; i++) {
            if (distancesCurrentRow[i] < minValue) {
                minValue = distancesCurrentRow[i];
                seamIndex = i;
            }
        }

        int [] seam = new int [height()];
        seam[seam.length-1] = seamIndex;
        for (int i = height() - 2; i >= 0; i--) {
            seamIndex = paths[i][seamIndex];
            seam[i] = seamIndex;
        }

        return seam;
    }

    private int [] edges(int x) {
        if (height() == 1) {
            return new int[] {x};
        }

        if (x == 0) {
            return new int [] {x, x + 1};
        } else if (x == width() - 1) {
            return new int [] {x - 1, x};
        } else {
            return new int[] {x - 1, x, x + 1};
        }
    }

    private double [][] computeEnergies() {
        double [][] energies = new double[height()][width()];

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energies[i][j] = energy(j, i);
            }
        }

        return energies;
    }

    private Picture transpose() {
        Picture original = picture();
        Picture transposed = new Picture(original.height(), original.width());

        for (int i = 0; i < original.width(); i++) {
            for (int j = 0; j < original.height(); j++) {
                transposed.set(j, i, original.get(i, j));
            }
        }

        return transposed;
    }

    /**
     * Remove horizontal seam from current picture.
     * */
    public void removeHorizontalSeam(int[] seam) {
        this.picture = transpose();

        try {
            removeVerticalSeam(seam);
        } finally {
            this.picture = transpose();
        }
    }

    /**
     * Remove vertical seam from current picture.
     */
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam);

        int smallerWidth = width() - 1;
        Picture smaller = new Picture(smallerWidth, height());

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < smallerWidth; j++) {
                if (j < seam[i]) {
                    smaller.set(j, i, picture.get(j, i));
                } else {
                    smaller.set(j, i, picture.get(j + 1, i));
                }
            }
        }

        this.picture = smaller;
    }

    private void validateSeam(int [] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam cannot be null.");
        }

        if (seam.length != height()) {
            throw new IllegalArgumentException(String.format("Invalid seam size: %d", seam.length));
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException(
                        String.format("Seam contains invalid coordinate %d at position %d.", seam[i], i));
            }

            if (i < seam.length - 1) {
                if (Math.abs(seam[i] - seam[i+1]) > 1) {
                    throw new IllegalArgumentException("Invalid seam - distance between coordinates > 1");
                }
            }
        }
    }
}