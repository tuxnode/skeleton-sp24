package deque;

import java.util.List;
import java.util.ArrayList;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    private static final int INITIAL_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public ArrayDeque61B() {
        items = (T[]) new Object[INITIAL_CAPACITY];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newItems = (T[]) new Object[newCapacity];
        int oldIndex = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newItems[i] = items[oldIndex];
            oldIndex = plusOne(oldIndex);
        }
        items = newItems;
        nextFirst = newCapacity - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        nextFirst = minusOne(nextFirst);
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast = plusOne(nextLast);
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        int index = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            list.add(items[index]);
            index = plusOne(index);
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int firstIndex = plusOne(nextFirst);
        T value = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = firstIndex;
        size--;
        if (items.length >= 16 && size < items.length / 4) {
            resize(items.length / 2);
        }
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = minusOne(nextLast);
        T value = items[lastIndex];
        items[lastIndex] = null;
        nextLast = lastIndex;
        size--;
        if (items.length >= 16 && size < items.length / 4) {
            resize(items.length / 2);
        }
        return value;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (plusOne(nextFirst) + index) % items.length;
        return items[actualIndex];
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }

    @Override
    public T peekFirst() {
        return items[nextFirst];
    }
}
