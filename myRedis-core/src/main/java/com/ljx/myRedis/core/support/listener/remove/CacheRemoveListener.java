package com.ljx.myRedis.core.support.listener.remove;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICacheRemoveListener;
import com.ljx.myRedis.api.ICacheRemoveListenerContext;


/**
 * 默认删除监听类
 */
public class CacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    private static final Log log = LogFactory.getLog(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(),context.value(),context.type());
    }
}
