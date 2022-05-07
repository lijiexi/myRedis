package com.ljx.myRedis.annotation;

import java.lang.annotation.*;

/**
 * 缓存拦截器注释
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {
    /**
     * 通用拦截器
     * 1.耗时统计
     * 2.慢日志统计
     * 默认开启
     */
    boolean common() default true;

    /**
     * 是否启用刷新
     * 默认关闭
     */
    boolean refresh() default false;

    /**
     * 该操作是否需要append to file
     * 针对 cache 内容有变更的操作，不包括查询操作。
     * 删除、添加、过期操作
     * @return 是否开启aof
     */
    boolean aof() default false;

    /**
     * 是否执行驱逐更新
     * 用户LRU/LFU驱逐策略
     * @return
     */
    boolean evict() default false;
}
