package com.ljx.myRedis.core.support.listener.slow;

import com.alibaba.fastjson.JSON;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICacheSlowListener;
import com.ljx.myRedis.api.ICacheSlowListenerContext;
import com.ljx.myRedis.core.support.interceptor.common.CacheInterceptorCost;

/**
 * 慢日志监听类
 */
public class CacheSlowListener implements ICacheSlowListener {
    private static final Log log = LogFactory.getLog(CacheInterceptorCost.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 0;
    }
}
