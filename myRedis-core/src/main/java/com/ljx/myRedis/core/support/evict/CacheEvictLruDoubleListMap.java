package com.ljx.myRedis.core.support.evict;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.api.ICacheEvictContext;
import com.ljx.myRedis.core.exception.CacheRuntimeException;
import com.ljx.myRedis.core.model.CacheEntry;
import com.ljx.myRedis.core.model.DoubleListNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 驱逐策略
 * lru 基于HashMap+双向链表
 */
public class CacheEvictLruDoubleListMap<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLruDoubleListMap.class);
    /**
     * 头节点
     */
    private DoubleListNode<K,V> head;
    /**
     * 尾节点
     */
    private DoubleListNode<K,V> tail;
    /**
     * 存储key和链表节点
     * 方便快速定位链表节点位置
     * 同时hashmap获取元素复杂度为O(1)
     */
    private Map<K,DoubleListNode<K,V>> indexMap;

    public CacheEvictLruDoubleListMap() {
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.head.next(this.tail);
        this.tail.pre(this.head);
    }
    @Override
    public ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        final ICache<K,V> cache = context.cache();
        //超过限制，则移除队尾元素
        if (cache.size() >= context.size()) {
            //获取尾巴前一个元素
            DoubleListNode<K,V> tailPre = this.tail.pre();
            if (tailPre == this.head) {
                log.error("当前链表为空，无法删除");
                throw new CacheRuntimeException("LRU删除错误");
            }
            K evictKey = tailPre.key();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey,evictValue);
            this.removeKey(evictKey);
        }
        return result;
    }

    /**
     * 更新元素
     * 1.删除已经存在元素
     * 2.在头部插入该元素]
     * 执行put，get，containsKey方法时
     * @param key
     */
    @Override
    public void updateKey(K key) {
        //1.在链表里删除
        this.removeKey(key);
        //2.新元素插入头部
        DoubleListNode node = new DoubleListNode();
        node.key(key);

        node.pre(head);
        node.next(head.next());
        head.next().pre(node);
        head.next(node);
        //3.插入到indexMap中
        indexMap.put(key,node);
    }

    /**
     * 执行remove方法时
     * 1.获取map中元素，不存在直接返回
     * 2.删除链表中该元素
     * 3.删除map中元素
     * @param key
     */
    @Override
    public void removeKey(K key) {
        DoubleListNode<K,V> node = indexMap.get(key);
        if (ObjectUtil.isNull(node)) {
            return;
        }
        //删除链表
        node.pre().next(node.next());
        node.next().pre(node.pre());
        //删除map
        this.indexMap.remove(key);
    }
}
