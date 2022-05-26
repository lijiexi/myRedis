package com.ljx.myRedis.core.support.struct.lru.impl;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.core.exception.CacheRuntimeException;
import com.ljx.myRedis.core.model.CacheEntry;
import com.ljx.myRedis.core.model.DoubleListNode;
import com.ljx.myRedis.core.support.struct.lru.ILruMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于双向链表实现
 * @param <K>
 * @param <V>
 */
public class LruMapDoubleList<K,V> implements ILruMap<K,V> {

    private static final Log log = LogFactory.getLog(LruMapDoubleList.class);

    /**
     * 头节点
     */
    private DoubleListNode<K,V> head;
    /**
     * 尾节点
     */
    private DoubleListNode<K,V> tail;
    /**
     * map
     * key: 元素信息
     * value: 元素在 list 中对应的节点信息
     */
    private Map<K,DoubleListNode<K,V>> indexMap;

    public LruMapDoubleList () {
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    public ICacheEntry<K, V> removeEldest() {
        // 获取尾巴节点的前一个元素
        DoubleListNode<K,V> tailPre = this.tail.pre();
        if (tailPre == this.head) {
            log.error("当前列表为空，无法进行删除操作");
            throw new CacheRuntimeException("不可删除头节点");
        }

        K evictKey = tailPre.key();
        V evictValue = tailPre.value();
        this.removeKey(evictKey);
        return CacheEntry.of(evictKey,evictValue);
    }

    /**
     * 放入元素
     *
     * （1）删除已经存在的
     * （2）新元素放到元素头部
     *
     * @param key 元素
     */
    @Override
    public void updateKey(K key) {
        //1.执行删除
        this.removeKey(key);

        //2.新元素放入头部
        DoubleListNode<K,V> newNode = new DoubleListNode<>();
        newNode.key(key);

        DoubleListNode<K,V> next = this.head.next();
        this.head.next(newNode);
        newNode.pre(this.head);
        next.pre(newNode);
        newNode.next(next);

        //3.插入到map
        indexMap.put(key,newNode);

    }

    /**
     * 移除元素
     * 1.获取map中元素，不存在直接返回
     * 2.删除双向链表中的元素
     * 3.删除map中该元素
     * @param key key
     */
    @Override
    public void removeKey(K key) {
        DoubleListNode<K,V> node = indexMap.get(key);

        if (ObjectUtil.isNull(node)) {
            return;
        }
        // 删除 list node
        // A<->B<->C
        // 删除 B，需要变成： A<->C
        DoubleListNode<K,V> pre = node.pre();
        DoubleListNode<K,V> next = node.next();

        pre.next(next);
        next.pre(pre);
        // 删除 map 中对应信息
        this.indexMap.remove(key);
        log.debug("从 LruMapDoubleList 中移除 key: {}", key);

    }

    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return indexMap.containsKey(key);
    }
}
