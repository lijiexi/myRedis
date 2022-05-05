package com.ljx.myRedis.core.support.proxy.bs;

import com.ljx.myRedis.annotation.CacheInterceptor;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheInterceptor;
import com.ljx.myRedis.api.ICachePersist;
import com.ljx.myRedis.core.support.interceptor.CacheInterceptorContext;
import com.ljx.myRedis.core.support.interceptor.CacheInterceptors;
import com.ljx.myRedis.core.support.persist.CachePersistAof;
import com.ljx.myRedis.core.support.proxy.CacheProxy;

import java.util.List;

/**
 * 代理引导类
 */
public class CacheProxyBs {

    private CacheProxyBs() {}

    /**
     * 代理引导类上下文
     */
    private ICacheProxyBsContext context;
    /**
     * 默认使用通用拦截器
     * 包括慢日志和时间消耗
     */
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.defaultCommonList();

    private final ICacheInterceptor persistInterceptors = CacheInterceptors.aof();
    public static CacheProxyBs newInstance() {
        return new CacheProxyBs();
    }
    public CacheProxyBs context (ICacheProxyBsContext context) {
        this.context = context;
        return this;
    }

    /**
     * 执行
     * @return
     * @throws Throwable
     */
    public Object execute() throws Throwable {
        //1.开始时间
        final long startMills = System.currentTimeMillis();
        final ICache cache = context.target();
        CacheInterceptorContext interceptorContext = CacheInterceptorContext.newInstance()
                .startMills(startMills)
                .method(context.method())
                .params(context.params())
                .cache(context.target());
        //1.获取刷新注解信息
        CacheInterceptor cacheInterceptor = context.interceptor();
        this.interceptorHandler(cacheInterceptor,interceptorContext,cache,true);

        //2.正常执行
        Object result = context.process();

        final long endMills = System.currentTimeMillis();
        interceptorContext.endMills(endMills).result(result);

        //3.方法执行完成
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, false);
        return result;
    }

    /**
     * 拦截器执行类
     * @param cacheInterceptor cache拦截器
     * @param interceptorContext 上下文
     * @param cache cache
     * @param before 判断执行before还是after
     */
    private void interceptorHandler (CacheInterceptor cacheInterceptor,
                                     CacheInterceptorContext interceptorContext,
                                     ICache cache,
                                     boolean before) {
        if (cacheInterceptor != null) {
            //1.通用拦截器，慢日志，耗时
            if (cacheInterceptor.common()) {
                for (ICacheInterceptor interceptor : commonInterceptors) {
                    if(before) {
                        interceptor.before(interceptorContext);
                    } else {
                        interceptor.after(interceptorContext);
                    }
                }
            }
            //TODO 刷新

            //2.AOF追加
            final ICachePersist cachePersist = cache.persist();
            //当持久化类为 AOF 模式时，并且方法开启aof时执行
            if (cacheInterceptor.aof() && cachePersist instanceof CachePersistAof) {
                if(before) {
                    persistInterceptors.before(interceptorContext);
                } else {
                    persistInterceptors.after(interceptorContext);
                }
            }
        }

    }
}
