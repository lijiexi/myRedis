package com.ljx.myRedis.core.support.persist;

import com.ljx.myRedis.api.ICachePersist;

/**
 * 选择持久化方式
 */
public class CachePersists {

    private CachePersists () {}

    /**
     * 无操作
     */
    public static <K, V> ICachePersist<K, V> none () {
        return new CachePersistNone<>();
    }

    /**
     * rdb持久化
     * @param path 持久化文件路径
     * @param <K> key
     * @param <V> value
     * @return 持久化结果
     */
    public static <K,V> ICachePersist<K,V> dbJson (final String path) {
        return new CachePersistDbJson<>(path);
    }
}
