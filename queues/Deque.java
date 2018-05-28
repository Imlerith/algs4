import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node front = null;
    private Node end = null;
    private int size = 0;
    public boolean isEmpty()                 // is the deque empty?
    {
        return size == 0;
    }
    public int size()                        // return the number of items on the deque
    {
        return size;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new IllegalArgumentException();
        Node tmp = front;
        front = new Node(item);
        if (tmp == null) {
            end = front;
        } else {
            front.next = tmp;
            tmp.prev = front;
        }
        size++;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new IllegalArgumentException();
        Node tmp = end;
        end = new Node(item);
        if (tmp == null) {
            front = end;
        } else {
            tmp.next = end;
            end.prev = tmp;
        }
        size++;
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node tmp = front;
        if (size == 1) {
            front = null;
            end = null;
        } else {
            front = tmp.next;
        }
        size--;
        return tmp.item;
    }
    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node tmp = end;
        if (size == 1) {
            front = null;
            end = null;
        } else {
            end = tmp.prev;
        }
        size--;
        return tmp.item;
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item> {
        Node current = front;
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    private class Node {
        Item item;
        Node next = null;
        Node prev = null;
        public Node(Item item) {
            this.item = item;
        }
    }
    public static void main(String[] args)   // unit testing (optional)
    {
        System.out.println("Creating new Deque...");
        Deque<String> testDeque = new Deque<String>();

        System.out.println("Current deque size: " + testDeque.size());

        if (testDeque.isEmpty()) { System.out.println("Deque empty."); }
        else { System.out.println("Deque not empty."); }

        System.out.println("\nAdding three strings to the Deque");
        testDeque.addFirst("String 1");
        testDeque.addFirst("String 2");
        testDeque.addLast("String 3");

        for (String i : testDeque) {
            System.out.println("Item: " + i);
        }
        System.out.println("Current deque size: " + testDeque.size());

        if (testDeque.isEmpty()) { System.out.println("Deque empty."); }
        else { System.out.println("Deque not empty."); }

        System.out.println("\nRemoving two strings from the Deque");
        System.out.println("Removing " + testDeque.removeFirst());
        System.out.println("Removing " + testDeque.removeLast());
        System.out.println("Current deque size: " + testDeque.size());

        if (testDeque.isEmpty()) { System.out.println("Deque empty."); }
        else { System.out.println("Deque not empty."); }

        System.out.println("\nRemoving one string from the Deque");
        System.out.println("Removing " + testDeque.removeFirst());

        System.out.println("\nAdding one string to the Deque");
        testDeque.addFirst("String 4");
        System.out.println("Current deque size: " + testDeque.size());

        System.out.println("\nRemoving one string from the Deque");
        System.out.println("Removing " + testDeque.removeLast());

        System.out.println("Current deque size: " + testDeque.size());

        if (testDeque.isEmpty()) { System.out.println("Deque empty."); }
        else { System.out.println("Deque not empty."); }
    }
}