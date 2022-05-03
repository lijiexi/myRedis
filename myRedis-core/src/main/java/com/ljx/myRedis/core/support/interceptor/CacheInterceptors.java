package com.ljx.myRedis.core.support.interceptor;

import com.ljx.myRedis.api.ICacheInterceptor;
import com.ljx.myRedis.core.support.interceptor.common.CacheInterceptorCost;

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
}
