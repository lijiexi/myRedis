package com.ljx.myRedis.core;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.ljx.myRedis.annotation.CacheInterceptor;
import com.ljx.myRedis.api.*;
import com.ljx.myRedis.core.constant.enums.CacheRemoveType;
import com.ljx.myRedis.core.exception.CacheRuntimeException;
import com.ljx.myRedis.core.support.evict.CacheEvictContext;
import com.ljx.myRedis.core.support.expire.CacheExpire;
import com.ljx.myRedis.core.support.listener.remove.CacheRemoveListener;
import com.ljx.myRedis.core.support.listener.remove.CacheRemoveListenerContext;
import com.ljx.myRedis.core.support.persist.InnerCachePersist;

import java.util.*;

public class Cache<K, V> implements ICache<K, V> {
    /**
     * Map
     */
    private Map<K, V> map;
    /**
     * 大小限制
     */
    private int sizeLimit;

    /**
     *  驱逐策略
     */
    private ICacheEvict<K, V> evict;

    /**
     * 过期策略
     */
    private ICacheExpire<K, V> expire;
    /**
     * 删除监听类
     */
    private List<ICacheRemoveListener<K,V>> removeListeners;

    /**
     * 慢日志监听类
     */
    private List<ICacheSlowListener> slowListeners;

    /**
     * 加载类
     */
    private ICacheLoad<K,V> load;
    /**
     * 持久化
     */
    private ICachePersist<K,V> persist;
    /**
     * 设置map实现
     */
    public Cache<K, V> map (Map<K, V> map) {
        this.map = map;
        return this;
    }
    /**
     * 设置大小限制
     */
    public Cache<K, V> sizeLimit (int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }
    /**
     * 设置驱逐策略
     */
    public Cache<K, V> evict (ICacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }
    /**
     * 获取驱逐策略
     */
    public ICacheEvict<K, V> evict() {
        return this.evict;
    }

    /**
     * 设置持久化策略
     */
    public void persist (ICachePersist<K,V> persist) {
        this.persist = persist;
    }
    @Override
    public ICacheLoad<K, V> load() {
        return load;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return removeListeners;
    }
    public Cache<K,V> removeListeners (List<ICacheRemoveListener<K,V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return slowListeners;
    }

    public Cache<K, V> slowListeners(List<ICacheSlowListener> slowListeners) {
        this.slowListeners = slowListeners;
        return this;
    }

    /**
     * 设置加载策略
     */
    public Cache<K, V> load (ICacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    /**
     * 初始化cache 被cacheBS调用
     */
    public void init () {
        this.expire = new CacheExpire<>(this);
        //进行加载策略load
        this.load.load(this);
        //初始化持久策略，每10分钟进行一侧
        if (this.persist != null) {
            new InnerCachePersist<>(this,persist);
        }
    }
    /**
     * n毫秒后过期
     * @param key key
     * @param timeInMills timeInMills毫秒后过期
     * @return this
     */
    public ICache<K, V> expire (final K key, final long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        return expireAt(key, expireTime);
    }

    /**
     * 指定时间过期
     * @param key key
     * @param timeInMills 时间戳
     * @return
     */
    public ICache<K, V> expireAt (final K key, final long timeInMills) {
        this.expire.expire(key,timeInMills);
        return this;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return this.expire;
    }

    /**
     * 返回当前map已使用的大小
     */
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @CacheInterceptor()
    public V get(Object key) {
        //惰性删除 刷新所有过期时间
        K genericKey = (K) key;
        //创建不可变List的单个元素
        this.expire.refreshExpire(Collections.singletonList(genericKey));
        return map.get(key);
    }

    @Override
    @CacheInterceptor()
    public V put(K key, V value) {
        //1 尝试驱逐
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);
        //返回驱逐后明细信息
        ICacheEntry<K, V> evictEntry = evict.evict(context);
        //此时有kv被淘汰，触发所有删除监听器
        if (ObjectUtil.isNotNull(evictEntry)) {
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance()
                    .key(evictEntry.key()).value(evictEntry.value())
                    .type(CacheRemoveType.EVICT.code());
            //遍历所有删除监听器，依次触发
            for (ICacheRemoveListener<K,V> listener : context.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }
        //2 判断驱逐后信息
        if (isSizeLimit()) {
            throw new CacheRuntimeException("队列已满，添加数据失败！");
        }
        //3 添加数据
        return map.put(key, value);
    }

    /**
     * 判断是否达到大小最大限度
     * @return 是否限制
     */
    private boolean isSizeLimit () {
        final int currentSize = this.size();
        return currentSize >= this.sizeLimit;
    }
    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
