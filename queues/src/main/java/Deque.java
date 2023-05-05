import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);

        Node currentFirst = first;
        first = new Node(item).joinNext(currentFirst);

        if (last == null) {
            last = first;
        }

        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        validateItem(item);

        Node currentLast = last;
        last = new Node(item).joinPrev(currentLast);

        if (isEmpty()) {
            first = last;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node currentFirst = first;
        first = currentFirst.next;

        if (isEmpty()) {
            last = null;
        } else {
            first.prev = null;
        }

        size--;

        return currentFirst.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node currentLast = last;
        last = last.prev;
        currentLast.next = null;

        if (last == null) {
            first = null;
        }

        size--;

        return currentLast.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {

        return new Iterator<Item>() {

            private Node next = first;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Node current = next;
                next = current.next;
                return current.item;
            }
        };
    }

    private void validateItem(final Item item) {
        if (item == null) {
            throw new NullPointerException("Null items are not allowed for insertion.");
        }
    }

    public static void main(String[] args) {

    }

    private class Node {

        final Item item;
        Node next;
        Node prev;

        Node(final Item item) {
            this.item = item;
            this.next = null;
        }

        private Node joinNext(Node nextNode) {
            this.next = nextNode;
            if (nextNode != null) {
                nextNode.prev = this;
            }

            return this;
        }

        private Node joinPrev(Node prevNode) {
            this.prev = prevNode;
            if (prevNode != null) {
                prevNode.next = this;
            }

            return this;
        }
    }
}
