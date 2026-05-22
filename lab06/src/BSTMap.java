import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // 使用递归实现键值对的插入
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        root = put(this.root, key, value);
    }

    private Node put(Node node, K key, V value) {
        // 找到空位，直接放置
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // 比当前节点Key小
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            // 相同则更新节点value
            node.value = value;
        }

        return node;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        return get(this.root, key);
    }
    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        return containsKey(root, key);
    }

    private boolean containsKey(Node node, K key) {
        if (node == null) {
            return false;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> ret = new LinkedHashSet<>();
        fillKeys(root, ret);
        return ret;
    }

    private void fillKeys(Node node, Set<K> set) {
        if (node == null) {
            return;
        }
        fillKeys(node.left, set);
        set.add(node.key);
        fillKeys(node.right, set);
    }

    @Override
public V remove(K key) {
    if (!containsKey(key)) return null;
    V returnVal = get(key);
    root = remove(root, key);
    size--;
    return returnVal;
}

private Node remove(Node node, K key) {
    if (node == null) return null;

    int cmp = key.compareTo(node.key);
    if (cmp < 0)      node.left = remove(node.left, key);
    else if (cmp > 0) node.right = remove(node.right, key);
    else {
        // 找到了要删除的节点！开始判断三种情况：
        if (node.left == null)  return node.right; // 情况 1 & 2
        if (node.right == null) return node.left;  // 情况 2

        // 情况 3：有两个子节点
        Node successor = min(node.right); // 找到右子树最小的
        node.key = successor.key;
        node.value = successor.value;
        node.right = remove(node.right, successor.key); // 删掉那个顶替上来的后继节点
    }
    return node;
}

    private Node min(Node node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}