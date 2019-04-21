/******************************************************************************
 *  Name:    MaGuilong
 *
 *  Description:
 *      Thinking of random access, use array list for constant access.
 *
 *  Written:       20/01/2019
 *  Last updated:  20/01/2019
 *
 *
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int head = 0, tail = 0;
    private Item[] randomizedQueue;
    private void resize(int N) {
        Item[] newList = (Item[]) new Object[N];
        for (int i = 0; i < N && i < randomizedQueue.length; ++i) {
            newList[i] = randomizedQueue[i];
        }
        randomizedQueue = newList;
    }
    private boolean isFull() {
        return (tail - head + randomizedQueue.length) % randomizedQueue.length + 1 == randomizedQueue.length;
    }
    public RandomizedQueue() {                 // construct an empty randomized queue
        randomizedQueue = (Item[]) new Object[2];
    }
    public boolean isEmpty() {                 // is the randomized queue empty?
        return head == tail;
    }
    public int size() {                        // return the number of items on the randomized queue
        return (tail - head + randomizedQueue.length) % randomizedQueue.length;
    }
    public void enqueue(Item item) {           // add the item
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isFull()) {
            resize(randomizedQueue.length * 2);
        }
        randomizedQueue[tail] = item;
        tail = (tail + 1) % randomizedQueue.length;
    }
    public Item dequeue() {                    // remove and return a random item
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (randomizedQueue.length / 4 >= (tail - head + randomizedQueue.length) %  randomizedQueue.length) {
            resize(randomizedQueue.length / 2);
        }
        int random = StdRandom.uniform((tail - head + randomizedQueue.length) % randomizedQueue.length);
        random = (head + random + randomizedQueue.length) % randomizedQueue.length;
        Item item = randomizedQueue[random];
        randomizedQueue[random] = randomizedQueue[tail - 1];
        randomizedQueue[tail - 1] = null;
        //for (int i = random; (i + randomizedQueue.length) % randomizedQueue.length < tail - 1; ++i) {
        //    randomizedQueue[i] = randomizedQueue[i + 1];
        //}
        tail = (tail - 1 + randomizedQueue.length) % randomizedQueue.length;
        return item;
    }
    public Item sample() {                     // return a random item (but do not remove it)
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int random = StdRandom.uniform((tail - head + randomizedQueue.length) % randomizedQueue.length);
        random = (head + random + randomizedQueue.length) % randomizedQueue.length;
        return randomizedQueue[random];
    }
    public Iterator<Item> iterator() {         // return an independent iterator over items in random order
        return new RandomizedIterator();
    }
    private class RandomizedIterator implements Iterator<Item> {
        int totalSize = (tail - head + randomizedQueue.length) % randomizedQueue.length;
        int[] shuffle = new int[totalSize];
        RandomizedIterator() {
            for (int i = 0; i < shuffle.length; ++i) {
                shuffle[i] = i;
            }
            StdRandom.shuffle(shuffle);
        }
        boolean[] flag = new boolean[totalSize];
        public boolean hasNext() {
            return totalSize != 0;
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int random = (head + shuffle[totalSize - 1] + randomizedQueue.length) % randomizedQueue.length;
            //int random = StdRandom.uniform((tail - head + randomizedQueue.length) % randomizedQueue.length);
            //while(flag[random]) {
            //    random = StdRandom.uniform((tail - head + randomizedQueue.length) % randomizedQueue.length);
            //}
            //flag[random] = true;
            --totalSize;
            return randomizedQueue[random];
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    public static void main(String[] args) {   // unit testing (optional)
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        queue.enqueue("6");
        for (String str : queue) {
            System.out.print(str);
        }
        System.out.println();
        queue.dequeue();
        queue.dequeue();
        for (String str : queue) {
            System.out.print(str);
        }

        queue.enqueue("11");
        queue.enqueue("22");
        for (String str : queue) {
            System.out.print(str);
        }
        System.out.println();
        queue.dequeue();
        queue.dequeue();
        for (String str : queue) {
            System.out.print(str);
        }
    }

}
