package com.ljx.myRedis.api;

/**
 * 驱逐策略
 */
public interface ICacheEvict<K, V> {
    /**
     * 驱逐策略
     * @param context 上下文
     * @return 被移除的明细信息，没有时间则返回null
     */
    ICacheEntry<K, V> evict (final ICacheEvictContext<K, V> context);

    /**
     * 更新key信息
     */
    void updateKey (final K key);

    /**
     * 删除key信息
     */
    void removeKey(final K key);
}

