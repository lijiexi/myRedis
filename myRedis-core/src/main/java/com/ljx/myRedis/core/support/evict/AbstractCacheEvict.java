package com.ljx.myRedis.core.support.evict;

import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.api.ICacheEvict;
import com.ljx.myRedis.api.ICacheEvictContext;

/**
 * 丢弃策略 抽象实现类
 */
public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        //返回移除结果
        return doEvict(context);
    }

    /**
     * 进行数据移除
     * @param context 上下文
     * @return 结果
     */
    public abstract ICacheEntry<K, V> doEvict (ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
