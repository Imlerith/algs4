import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            q.enqueue(str);
        }
        while (size-- > 0)
            StdOut.print(q.dequeue() + "\n");
    }
}