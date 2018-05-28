import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;

    public RandomizedQueue()
    {
        a = (Item[]) new Object[2];
        n = 0;
    }
    public boolean isEmpty()
    {
        return n == 0;
    }
    public int size()
    {
        return n;
    }

    private void resize(int capacity) {

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    public void enqueue(Item item)
    {
        if (n == a.length) resize(2*a.length);
        a[n++] = item;
    }
    public Item dequeue()
    {
        int idx = StdRandom.uniform(n);

        Item item = a[idx];
        a[idx] = a[n - 1];
        a[--n] = null;
        if (n <= (a.length / 4) && (a.length / 2) > 0) {
            resize(a.length / 2);
        }
        return item;
    }
    public Item sample()
    {
        if (n == 0) throw new NoSuchElementException();
        int idx = StdRandom.uniform(n);
        return a[idx];
    }
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] random = (Item[]) new Object[n];
        private int idx = 0;

        public RandomizedQueueIterator() {
            int len = random.length;

            for (int i = 0; i < len; i++) {
                random[i] = a[i];
            }

            for (int i = 0; i < len; ++i) {
                int r = i + StdRandom.uniform(len - i);
                Item temp = random[i];
                random[i] = random[r];
                random[r] = temp;
            }
        }

        public boolean hasNext() {
            return idx < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return random[idx++];
        }
    }
    public static void main(String[] args)
    {
        System.out.println("Creating new RandQueue...");
        RandomizedQueue<Double> testRandQueue = new RandomizedQueue<Double>();

        System.out.println("Current RandQueue size: " + testRandQueue.size());

        if (testRandQueue.isEmpty()) { System.out.println("RandQueue empty."); }
        else { System.out.println("RandQueue not empty."); }

        System.out.println("\nAdding five doubles to the RandQueue");
        testRandQueue.enqueue(1.0);
        testRandQueue.enqueue(2.0);
        testRandQueue.enqueue(3.0);
        testRandQueue.enqueue(4.0);
        testRandQueue.enqueue(5.0);

        System.out.println("\nRandom printing once...");
        for (double i : testRandQueue) {
            System.out.println("Item: " + i);
        }

        System.out.println("\nRandom printing twice...");
        for (double i : testRandQueue) {
            System.out.println("Item: " + i);
        }

        System.out.println("\nOne more for good luck...");
        for (double i : testRandQueue) {
            System.out.println("Item: " + i);
        }

        System.out.println("\nCurrent RandQueue size: " + testRandQueue.size());

        if (testRandQueue.isEmpty()) { System.out.println("RandQueue empty."); }
        else { System.out.println("RandQueue not empty."); }

        System.out.println("\nRemoving two doubles from the RandQueue");
        System.out.println("Removing " + testRandQueue.dequeue());
        System.out.println("Removing " + testRandQueue.dequeue());
        System.out.println("Current RandQueue size: " + testRandQueue.size());

        System.out.println("\nRandom printing once...");
        for (double i : testRandQueue) {
            System.out.println("Item: " + i);
        }

        System.out.println("Random Sample: " + testRandQueue.sample());
        System.out.println("Random Sample: " + testRandQueue.sample());

        if (testRandQueue.isEmpty()) { System.out.println("RandQueue empty."); }
        else { System.out.println("RandQueue not empty."); }

        System.out.println("\nRemoving two doubles from the RandQueue");
        System.out.println("Removing " + testRandQueue.dequeue());
        System.out.println("Removing " + testRandQueue.dequeue());
        System.out.println("Current RandQueue size: " + testRandQueue.size());

        System.out.println("Removing " + testRandQueue.dequeue());
        System.out.println("Current RandQueue size: " + testRandQueue.size());

        if (testRandQueue.isEmpty()) { System.out.println("RandQueue empty."); }
        else { System.out.println("RandQueue not empty."); }
    }
}