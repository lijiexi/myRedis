package com.ljx.myRedis.core.model;

/**
 * 双向链表节点
 */
public class DoubleListNode<K,V> {
    /**
     * key
     */
    private K key;
    /**
     * value
     */
    private V value;
    /**
     * pre node
     */
    private DoubleListNode<K,V> pre;
    /**
     * next node
     */
    private DoubleListNode<K,V> next;
    public K key() {
        return key;
    }

    public DoubleListNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public DoubleListNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public DoubleListNode<K, V> pre() {
        return pre;
    }

    public DoubleListNode<K, V> pre(DoubleListNode<K, V> pre) {
        this.pre = pre;
        return this;
    }

    public DoubleListNode<K, V> next() {
        return next;
    }

    public DoubleListNode<K, V> next(DoubleListNode<K, V> next) {
        this.next = next;
        return this;
    }
}
