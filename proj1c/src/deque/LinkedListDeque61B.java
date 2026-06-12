package deque;

import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private class Node {
        public T item;
        public Node prev;
        public Node next;

        public Node(T i, Node prev, Node next) {
            this.item = i;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinel;
    private int dequeSize;

    @Override
    public void addFirst(T x) {
        Node node = new Node(x, sentinel, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        dequeSize++;
    }

    @Override
    public void addLast(T x) {
        Node node = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        dequeSize++;
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node curr = sentinel.next;
        while (curr != sentinel) {
            list.add(curr.item);
            curr = curr.next;
        }

        return list;
    }

    @Override
    public boolean isEmpty() {
        return dequeSize == 0;
    }

    @Override
    public int size() {
        return dequeSize;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        T value = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        dequeSize--;
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) return null;
        T value = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        dequeSize--;
        return value;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= dequeSize) return null;
        Node iter = sentinel.next;
        for (int i = 0; i < index; i++) {
            iter = iter.next;
        }
        return iter.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= dequeSize) return null;

        return getRecursiveHelper(sentinel.next, index);
    }

    @Override
    public T peekFirst() {
        if (isEmpty()) return null;
        return sentinel.next.item;
    }

    private T getRecursiveHelper(Node curr, int index) {
        if (index == 0) {
            return curr.item;
        }
        return getRecursiveHelper(curr.next, index - 1);
    }

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        dequeSize = 0;
    }
}
