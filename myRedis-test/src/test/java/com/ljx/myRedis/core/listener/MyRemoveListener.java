package com.ljx.myRedis.core.listener;

import com.ljx.myRedis.api.ICacheRemoveListener;
import com.ljx.myRedis.api.ICacheRemoveListenerContext;

/**
 * 自定义删除监听器
 */
public class MyRemoveListener<K,V> implements ICacheRemoveListener<K,V> {
    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        System.out.println(context.key()+" 已被删除!");

    }
}
