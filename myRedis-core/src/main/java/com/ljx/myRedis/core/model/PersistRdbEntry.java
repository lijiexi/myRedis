package com.ljx.myRedis.core.model;

/**
 * rdb持久化明细
 */
public class PersistRdbEntry<K, V> {
    /**
     * key
     */
    private K key;
    /**
     * value
     */
    private V value;
    /**
     * 过期时间
     */
    private Long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
