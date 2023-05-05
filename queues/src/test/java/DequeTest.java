import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeTest {

    @Test
    public void testEmptyDeque() {
        Deque<Integer> deque = new Deque<>();

        Assertions.assertThat(deque.isEmpty()).isTrue();
        Assertions.assertThat(deque.size()).isEqualTo(0);
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> deque.removeFirst());
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> deque.removeLast());
    }

    @Test
    public void testEmptyIterator() {
        Deque<Integer> deque = new Deque<>();

        Assertions.assertThat(deque.iterator().hasNext()).isFalse();
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> deque.iterator().next());
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> deque.iterator().remove());
    }


    @Test
    public void testNonEmptyIterator() {
        Deque<Integer> deque = new Deque<>();

        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        Iterator<Integer> iterator = deque.iterator();

        Assertions.assertThat(iterator.hasNext()).isTrue();
        Assertions.assertThat(iterator.next()).isEqualTo(1);
        Assertions.assertThat(iterator.next()).isEqualTo(2);
        Assertions.assertThat(iterator.next()).isEqualTo(3);
        Assertions.assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testAddingAndRemovingFirst() {
        Deque<Integer> deque = new Deque<>();

        deque.addFirst(1);
        Assertions.assertThat(deque.isEmpty()).isFalse();
        Assertions.assertThat(deque.removeFirst()).isEqualTo(1);
        Assertions.assertThat(deque.isEmpty()).isTrue();
    }

    @Test
    public void testAddingAndRemovingLast() {
        Deque<Integer> deque = new Deque<>();

        deque.addLast(1);
        Assertions.assertThat(deque.isEmpty()).isFalse();
        Assertions.assertThat(deque.removeLast()).isEqualTo(1);
        Assertions.assertThat(deque.isEmpty()).isTrue();
    }

    @Test
    public void testAddingAndRemoving() {
        Deque<Integer> deque = new Deque<>();

        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(3);
        deque.addLast(4);

        Assertions.assertThat(deque.removeFirst()).isEqualTo(1);
        Assertions.assertThat(deque.removeFirst()).isEqualTo(2);
        Assertions.assertThat(deque.removeLast()).isEqualTo(4);

        deque.addLast(5);

        Assertions.assertThat(deque.removeFirst()).isEqualTo(3);
        Assertions.assertThat(deque.removeFirst()).isEqualTo(5);
        Assertions.assertThat(deque.isEmpty()).isTrue();
    }
}
