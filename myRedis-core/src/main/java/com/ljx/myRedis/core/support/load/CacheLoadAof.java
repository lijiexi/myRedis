package com.ljx.myRedis.core.support.load;

import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.annotation.CacheInterceptor;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheLoad;
import com.ljx.myRedis.core.Cache;
import com.ljx.myRedis.core.model.PersistAofEntry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * aof加载策略
 * 遍历文件内容，反射调用原来的方法
 */
public class CacheLoadAof<K,V> implements ICacheLoad<K,V> {

    private static final Log log = LogFactory.getLog(CacheLoadAof.class);
    /**
     * 缓存cache中，所有开启了aof注解的方法到map里
     */
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    static {
        //获得所有方法
        Method[] methods = Cache.class.getMethods();

        for (Method method : methods) {
            CacheInterceptor cacheInterceptor = method.getAnnotation(CacheInterceptor.class);
            if (cacheInterceptor != null) {
                //方法开启了aof
                if(cacheInterceptor.aof()) {
                    String methodName = method.getName();

                    METHOD_MAP.put(methodName,method);
                }
            }
        }
    }

    /**
     * 文件路径
     */
    private final String dbPath;

    public CacheLoadAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[aof加载] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[aof加载] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for (String line : lines) {
            if (StringUtil.isEmpty(line)) {
                continue;
            }
            //执行
            PersistAofEntry entry = JSON.parseObject(line,PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            final Method method = METHOD_MAP.get(methodName);
            //调用反射，执行原来的方法
            ReflectMethodUtil.invoke(cache,method,objects);

        }
    }
}
