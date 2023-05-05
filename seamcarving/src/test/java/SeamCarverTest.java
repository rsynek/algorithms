import edu.princeton.cs.algs4.Picture;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SeamCarverTest {

    private static final String PICTURE_FOLDER = "data";

    @Test
    public void testEnergy1x1() {
        SeamCarver seamCarver = new SeamCarver(readPicture("1x1.png"));

        Assertions.assertThat(seamCarver.energy(0, 0)).isEqualTo(1000);
    }

    @Test
    public void testEnergy3x4() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x4.png"));

        // energy on the border
        Assertions.assertThat(seamCarver.energy(0, 0)).isEqualTo(1000);
        Assertions.assertThat(seamCarver.energy(2, 3)).isEqualTo(1000);
        Assertions.assertThat(seamCarver.energy(2, 1)).isEqualTo(1000);
        Assertions.assertThat(seamCarver.energy(1, 3)).isEqualTo(1000);

        // energy inside the border
        Assertions.assertThat(seamCarver.energy(1, 1)).isBetween(228.0, 229.0);
        Assertions.assertThat(seamCarver.energy(1, 2)).isBetween(228.0, 229.0);
    }

    @Test
    public void testVerticalSeam3x4() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x4.png"));

        Assertions.assertThat(seamCarver.findVerticalSeam()).containsSequence(0, 1, 1, 0);
    }

    @Test
    public void testVerticalSeam3x7() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x7.png"));

        Assertions.assertThat(seamCarver.findVerticalSeam()).containsSequence(0, 1, 1, 1, 1, 1, 0);
    }

    @Test
    public void testVerticalSeam7x10() {
        SeamCarver seamCarver = new SeamCarver(readPicture("7x10.png"));

        Assertions.assertThat(seamCarver.findVerticalSeam()).containsSequence(2, 3, 4, 3, 4, 3, 3, 2, 2, 1);
    }

    @Test
    public void testHorizontalSeam3x4() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x4.png"));

        Assertions.assertThat(seamCarver.findHorizontalSeam()).containsSequence(1, 2, 1);
    }

    @Test
    public void testRemoveMiddleVerticalSeamFrom3x4() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x4.png"));
        seamCarver.removeVerticalSeam(new int[] {1, 1, 1, 1});

        Picture resized = seamCarver.picture();
        Assertions.assertThat(resized.width()).isEqualTo(2);
        Assertions.assertThat(resized.height()).isEqualTo(4);
    }

    @Test
    public void testRemoveRightMostVerticalSeamFrom3x4() {
        SeamCarver seamCarver = new SeamCarver(readPicture("3x4.png"));
        seamCarver.removeVerticalSeam(new int[] {2, 2, 2, 2});

        Picture resized = seamCarver.picture();
        Assertions.assertThat(resized.width()).isEqualTo(2);
        Assertions.assertThat(resized.height()).isEqualTo(4);
    }

    private Picture readPicture(String name) {
        return new Picture(PICTURE_FOLDER + "/" + name);
    }
}
