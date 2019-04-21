/******************************************************************************
 *  Name:    MaGuilong
 *
 *  Description:
 *
 *  Written:       20/01/2019
 *  Last updated:  20/01/2019
 *
 *
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int cnt = 0;
    private class Node {
        Node pre;
        Node next;
        Item item;
    }
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    public Deque() {                           // construct an empty deque
        first = null;
        last = null;
    }
    public boolean isEmpty() {                 // is the deque empty?
        return first == null || last == null;
    }
    public int size() {                        // return the number of items on the deque
        return cnt;
    }
    public void addFirst(Item item) {          // add the item to the front
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.pre = null;
        first.next = oldFirst;
        if (isEmpty()) {
            last = first;
        } else {
            oldFirst.pre = first;
        }
        ++cnt;
    }
    public void addLast(Item item) {           // add the item to the end
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.pre = oldLast;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        ++cnt;
    }
    public Item removeFirst() {                // remove and return the item from the front
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node oldFirst = first;
        Item item = first.item;
        first = first.next;
        oldFirst.item = null;
        oldFirst.next = null;
        oldFirst.pre = null;
        if (isEmpty()) {
            last = first;
        } else {
            first.pre = null;
        }
        --cnt;
        return item;
    }
    public Item removeLast() {                 // remove and return the item from the end
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node oldLast = last;
        Item item = last.item;
        last = last.pre;
        oldLast.next = null;
        oldLast.pre = null;
        oldLast.item = null;
        if (isEmpty()) {
            first = last;
        } else {
            last.next = null;
        }
        --cnt;
        return item;
    }
    public Iterator<Item> iterator() {         // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    public static void main(String[] args) {   // unit testing (optional)
        Deque<String> deque = new Deque<>();
        deque.addFirst("1");
        deque.addLast("2");
        deque.addLast("3");
        System.out.println("add 1, 2, 3:\n");
        for (String str : deque) {
            System.out.print(str);
        }
        System.out.println();
        deque.removeFirst();
        System.out.println("remove first\n");
        for (String str : deque) {
            System.out.print(str);
        }
        deque.removeLast();
        System.out.println("remove last\n");
        for (String str : deque) {
            System.out.print(str);
        }
    }
}