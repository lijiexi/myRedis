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
}
