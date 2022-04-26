package com.ljx.myRedis.core.support.expire;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheExpire;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期 定时删除优化 时间排序策略
 *
 * 优点： 定时删除消耗减少
 * 缺点： 惰性删除不友好
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireSort<K, V> implements ICacheExpire<K, V> {

    /**
     * 单次清空数量限制
     */
    private static final int LIMIT = 100;

    /**
     * 按照过期时间排序存储过期缓存
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return (int) (o1 - o2);
        }
    });

    /**
     * 过期map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort (ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     */
    private void init ()
    {
        service.scheduleAtFixedRate(new ExpireThread(),100,100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     */
    private class ExpireThread implements Runnable {
        @Override
        public void run() {
            //1.判断map是否为空
            if (null == sortMap || 0 == sortMap.size()) {
                return;
            }
            //2.获取key进行处理
            int count = 0;
            for (Map.Entry<Long,List<K>> entry:sortMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                //判断某个过期时间下的队列是否为空
                if (null == expireKeys || expireKeys.isEmpty()) {
                    sortMap.remove(expireAt);
                    continue;
                }
                if (count >= LIMIT) {
                    return;
                }
                //进行删除
                long currentTime = System.currentTimeMillis();
                if (currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        //移除本身
                        iterator.remove();
                        expireMap.remove(key);
                        cache.remove(key);
                        count++;
                    }
                } else {
                    //之后的kv都未过期
                    return;
                }
            }
        }
    }
    @Override
    public void expire(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if (keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);
        //设置对应时间
        sortMap.put(expireAt,keys);
        expireMap.put(key,expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (null == keyList || keyList.isEmpty()) {
            return;
        }
        // 这样维护两套的代价太大
        // 判断大小，小的作为外循环
        final int expireSize = expireMap.size();
        if(expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for(Map.Entry<K,Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for(K key : keyList) {
                this.removeExpireKey(key);
            }
        }
    }
    /**
     * 移除过期信息
     * @param key key
     */
    private void removeExpireKey(final K key) {
        Long expireTime = expireMap.get(key);
        if(expireTime != null) {
            final long currentTime = System.currentTimeMillis();
            if(currentTime >= expireTime) {
                expireMap.remove(key);

                List<K> expireKeys = sortMap.get(expireTime);
                expireKeys.remove(key);
                sortMap.put(expireTime, expireKeys);
            }
        }
    }
}
