package com.ljx.myRedis.core.support.listener.slow;

import com.ljx.myRedis.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 慢日志监听工具类
 */
public final class CacheSlowListeners {
    private CacheSlowListeners () {}

    /**
     * 无
     * @return
     */
    public static List<ICacheSlowListener> none () {
        return new ArrayList<>();
    }

    /**
     * 默认慢日志监听类
     * @return
     */
    public static ICacheSlowListener defaults () {
        return new CacheSlowListener();
    }
}
