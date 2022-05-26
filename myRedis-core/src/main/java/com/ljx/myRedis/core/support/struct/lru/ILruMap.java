package com.ljx.myRedis.core.support.struct.lru;

import com.ljx.myRedis.api.ICacheEntry;

/**
 * LRU Map接口
 */
public interface ILruMap<K,V> {
    /**
     * 移除最老的元素
     * @return 移除元素明细
     */
    ICacheEntry<K,V> removeEldest ();

    /**
     * 更新 key 信息
     * @param key key
     */
    void updateKey (final K key);

    /**
     * 移除对应key信息
     * @param key key
     */
    void removeKey (final K key);

    /**
     * 判断是否为空
     * @return 是否为空
     */
    boolean isEmpty ();

    /**
     * 是否包含元素
     * @param key key
     * @return 结果
     */
    boolean contains (final K key);
}
