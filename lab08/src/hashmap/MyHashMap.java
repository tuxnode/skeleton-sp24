package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author tuxnode
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class HashMapIter implements Iterator<K> {
        private int bucketIndex;
        private Iterator<Node> nodeIterator;

        HashMapIter() {
            bucketIndex = 0;
            nodeIterator = getNodeIterator();
        }

        private Iterator<Node> getNodeIterator() {
            while (bucketIndex < buckets.length) {
                Collection<Node> bucket = buckets[bucketIndex];
                bucketIndex++;

                if (bucket != null && !bucket.isEmpty()) {
                    return bucket.iterator();
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            while (nodeIterator != null && !nodeIterator.hasNext()) {
                nodeIterator = getNodeIterator();
            }
            return nodeIterator != null;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return nodeIterator.next().key;
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }

        private K getKey() {
            return key;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor; // 负载因子

    private static final int DEFAULT_INIT_CAPASITY = 16;
    private static final double DEFAULT_LOADFACTOR = 0.75;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INIT_CAPASITY, DEFAULT_LOADFACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOADFACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.size = 0;
        this.loadFactor = loadFactor;
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            this.buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private int getIndex(K key) {
        return getIndex(key, buckets.length);
    }

    private int getIndex(K key, int capacity) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) return;

        int index = getIndex(key);
        Node newNode = new Node(key, value);
        Collection<Node> bucket = this.buckets[index];
        // Update Value if key had already set.
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        bucket.add(newNode);
        this.size++;

        if ((double) this.size / this.buckets.length > this.loadFactor) {
            resize(this.buckets.length * 2);
        }
    }

    private void resize(int newCapacity) {
        Collection<Node>[] newBuckets = (Collection<Node>[]) new Collection[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = createBucket();
        }

        for (Collection<Node> bucket : this.buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    int newIndex = getIndex(node.key, newCapacity);
                    newBuckets[newIndex].add(node);
                }
            }
        }
        this.buckets = newBuckets;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public V get(K key) {
        if (key == null) return null;
        int index = getIndex(key);

        Collection<Node> bucket = this.buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) return false;

        int index = getIndex(key);
        Collection<Node> bucket = this.buckets[index];

        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.size = 0;
        int currentBucketsLen = this.buckets.length;
        this.buckets = (Collection<Node>[]) new Collection[currentBucketsLen];

        for (int i = 0; i < currentBucketsLen; i++) {
            this.buckets[i] = createBucket();
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Collection<Node> iter : this.buckets) {
            if (iter != null) {
                for (Node node : iter) {
                    keys.add(node.key);
                }
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        if (key == null) return null;
        int index = getIndex(key);
        Collection<Node> bucket = this.buckets[index];
        if (bucket == null) {
            return null;
        }
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                V ret = node.value;
                bucket.remove(node);
                this.size--;
                return ret;
            }
        }
        return null;
    }
}
