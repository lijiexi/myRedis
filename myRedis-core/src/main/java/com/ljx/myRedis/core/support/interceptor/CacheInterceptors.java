package com.ljx.myRedis.core.support.interceptor;

import com.ljx.myRedis.api.ICacheInterceptor;
import com.ljx.myRedis.core.support.interceptor.aof.CacheInterceptorAof;
import com.ljx.myRedis.core.support.interceptor.common.CacheInterceptorCost;
import com.ljx.myRedis.core.support.interceptor.evict.CacheInterceptorEvict;

import java.util.ArrayList;
import java.util.List;

/**
 * cache拦截器工具类
 */
public class CacheInterceptors {
    /**
     * 默认通用
     * @return 耗时统计拦截器/慢日志
     */
    public static List<ICacheInterceptor> defaultCommonList() {
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }

    /**
     * AOF 模式
     * @return aof拦截器
     */
    public static ICacheInterceptor aof () {
        return new CacheInterceptorAof();
    }

    /**
     * 驱逐策略拦截器
     */
    public static ICacheInterceptor evict () {
        return new CacheInterceptorEvict();
    }
}
