package com.ljx.myRedis.api;

import java.util.List;
import java.util.Map;

/**
 * 缓存接口，兼容Map
 * 实现一个固定大小的缓存
 */
public interface ICache<K, V> extends Map<K, V> {

    /**
     * 淘汰策略
     * @return 淘汰
     * @since 0.0.11
     */
    ICacheEvict<K,V> evict();

    /**
     * 设置过期时间：如果key不存在则不操作
     * 1.惰性删除
     * 执行以下操作，发现过期则删除：
     * get(Object) 获取
     * values() 获取所有值
     * entrySet() 获取所有明细
     * 可能出现的问题：
     * 调用isEmpty()和size()方法，可能不是实时的结果，因为expire信息没有及时更新
     * 解决：考虑添加refresh等方法，暂时不做一致性的考虑。 因为实际使用更关心kv值
     *
     * 2.定时删除
     * 启动一个定时任务。每次随机选择指定大小的 key 进行是否过期判断。
     * 类似于 redis，为了简化，可以考虑设定超时时间，频率与超时时间成反比。
     * 此处默认使用 TTL 作为比较的基准，不支持 LastAccessTime 的淘汰策略。会增加复杂度。
     */

    /**
     * n毫米后过期
     * @param key key
     * @param timeInMills timeInMills毫秒后过期
     * @return this
     */
    ICache<K, V> expire (final K key, final long timeInMills);

    /**
     * 指定时间过期
     * @param key key
     * @param timeInMills 时间戳
     * @return this
     */
    ICache<K, V> expireAt (final K key, final long timeInMills);

    /**
     * 获取缓存的过期处理类
     * @return 返回过期处理类
     */
    ICacheExpire<K, V> expire ();

    /**
     * 返回删除监听器列表
     * @return 删除监听器类列表
     */
    List<ICacheRemoveListener<K,V>> removeListeners();
    /**
     * 加载信息
     * @return 加载信息
     */
    ICacheLoad<K, V> load ();

}
