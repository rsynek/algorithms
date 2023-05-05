import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueueTest {

    private static final RandomizedQueue<Integer> EMPTY_RANDOMIZED_QUEUE = new RandomizedQueue<>();
    private RandomizedQueue<Integer> testQueue;

    @Before
    public void prepare() {
        testQueue = new RandomizedQueue<>();
        testQueue.enqueue(1);
        testQueue.enqueue(2);
        testQueue.enqueue(3);
    }

    @Test
    public void testEmptyRandomizedQueue() {
        Assertions.assertThat(EMPTY_RANDOMIZED_QUEUE.isEmpty()).isTrue();
        Assertions.assertThat(EMPTY_RANDOMIZED_QUEUE.size()).isEqualTo(0);
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> EMPTY_RANDOMIZED_QUEUE.dequeue());
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> EMPTY_RANDOMIZED_QUEUE.sample());
    }

    @Test
    public void testEmptyIterator() {
        Assertions.assertThat(EMPTY_RANDOMIZED_QUEUE.iterator().hasNext()).isFalse();
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> EMPTY_RANDOMIZED_QUEUE.iterator().next());
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> EMPTY_RANDOMIZED_QUEUE.iterator().remove());
    }


    @Test
    public void testNonEmptyIterator() {
        Iterator<Integer> iterator = testQueue.iterator();

        Assertions.assertThat(iterator.hasNext()).isTrue();
        Assertions.assertThat(iterator.next()).isBetween(1, 3);
        Assertions.assertThat(iterator.next()).isBetween(1, 3);
        Assertions.assertThat(iterator.next()).isBetween(1, 3);
        Assertions.assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testAddingAndRemoving() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

        randomizedQueue.enqueue(1);
        Assertions.assertThat(randomizedQueue.isEmpty()).isFalse();
        Assertions.assertThat(randomizedQueue.dequeue()).isEqualTo(1);
        Assertions.assertThat(randomizedQueue.isEmpty()).isTrue();
    }

    @Test
    public void testSampling() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

        randomizedQueue.enqueue(1);
        Assertions.assertThat(randomizedQueue.isEmpty()).isFalse();
        Assertions.assertThat(randomizedQueue.sample()).isEqualTo(1);
        Assertions.assertThat(randomizedQueue.isEmpty()).isFalse();
    }
}
