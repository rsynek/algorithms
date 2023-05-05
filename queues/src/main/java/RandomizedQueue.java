import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_SIZE = 1;

    private Item[] items;
    private int lastIndex = -1;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item []) new Object[INITIAL_SIZE];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return lastIndex < 0;
    }

    // return the number of items on the queue
    public int size() {
        return lastIndex + 1;
    }

    // add the item
    public void enqueue(Item item) {
        validateItem(item);
        growIfNeeded();
        items[++lastIndex] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = randomIndex();
        Item item = items[index];
        items[index] = items[lastIndex];
        items[lastIndex--] = null;

        shrinkIfNeeded();

        return item;
    }

    private int randomIndex() {
        return StdRandom.uniform(lastIndex + 1);
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return items[randomIndex()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator(items, lastIndex + 1);
    }

    private void validateItem(final Item item) {
        if (item == null) {
            throw new NullPointerException("Null items are not allowed for insertion.");
        }
    }

    private void growIfNeeded() {
        if (lastIndex == items.length - 1) {
            resize (2 * items.length);
        }
    }

    private void shrinkIfNeeded() {
        if (!isEmpty() && (lastIndex + 1) == items.length / 4) {
            resize(items.length / 2);
        }
    }

    private void resize(int newCapacity) {
        int itemsToCopy = newCapacity > items.length ? items.length : newCapacity;

        Item[] newArray = (Item[]) new Object[newCapacity];
        for (int i = 0; i < itemsToCopy; i++) {
            newArray[i] = items[i];
        }
        items = newArray;
    }

    private class RandomizedIterator implements Iterator<Item> {

        private Item [] iteratorItems;
        private int index = 0;
        private int count;

        private RandomizedIterator(final Item[] data, int count) {
            iteratorItems = (Item []) new Object[count];
            for (int i = 0; i < count; i++) {
                iteratorItems[i] = data[i];
            }

            this.count = count;
            StdRandom.shuffle(iteratorItems);
        }

        @Override
        public boolean hasNext() {
            return index < count;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iteratorItems[index++];
        }
    }

    public static void main(String[] args) {

    }
}
