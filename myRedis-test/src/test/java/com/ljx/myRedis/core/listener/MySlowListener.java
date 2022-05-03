package com.ljx.myRedis.core.listener;

import com.ljx.myRedis.api.ICacheSlowListener;
import com.ljx.myRedis.api.ICacheSlowListenerContext;

/**
 * 自定义慢日志监听器
 */
public class MySlowListener implements ICacheSlowListener {
    @Override
    public void listen(ICacheSlowListenerContext context) {
        System.out.println("【慢日志】name: " + context.methodName());
    }

    @Override
    public long slowerThanMills() {
        return 0;
    }
}
