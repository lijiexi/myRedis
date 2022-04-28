package com.ljx.myRedis.core.support.expire;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheExpire;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期 定时删除策略
 * 将过期key作为键放入过期map，开启线程池，每隔100毫秒判断过期map是否为空
 * 如果不为空则从过期map和cache里删除100条过期数据。
 * @param <K> kye
 * @param <V> value
 */
public class CacheExpire<K, V> implements ICacheExpire<K, V> {

    /**
     * 轮询清理 单次清理数量限制
     */
    private static final int LIMIT = 100;

    /**
     * K 要过期的信息
     * Long 过期的时间
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;

    /**
     * 使用单线程，用来执行清空任务
     */
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire (ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化清理任务
     */
    private void init () {
        service.scheduleAtFixedRate(new ExpireThread(),100,100, TimeUnit.MILLISECONDS);
    }
    /**
     * 清空任务
     * 遍历过期数据，判断对应的时间，如果已经到期了，则执行清空操作
     * 避免单次执行时间过长，最多只处理 100 条
     */
    private class ExpireThread implements Runnable {
        @Override
        public void run() {
            //1.判断过期map是否为空
            if (null == expireMap || 0 == expireMap.size()) {
                return;
            }
            //2.获取key进行处理
            int count = 0;
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                if (count >= LIMIT) {
                    return;
                }
                expireKey(entry.getKey(),entry.getValue());
                count++;
            }
        }
    }

    /**
     * 指定过期信息
     * @param key key
     * @param expireAt 具体过期时间戳
     */
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key,expireAt);
    }

    private void expireKey (final K key, final Long expireAt) {
        if (expireAt == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            //移除缓存中kv
            cache.remove(key);
        }
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (null == keyList || keyList.isEmpty()) {
            return;
        }
        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if (keyList.size() <= expireMap.size()) {
            for (K key : keyList) {
                Long expireAt = expireMap.get(key);
                expireKey(key,expireAt);
            }
        } else {
            for (Map.Entry<K,Long> entry:expireMap.entrySet()) {
                this.expireKey(entry.getKey(),entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
