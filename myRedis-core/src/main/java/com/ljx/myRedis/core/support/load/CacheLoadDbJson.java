package com.ljx.myRedis.core.support.load;


import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheLoad;
import com.ljx.myRedis.core.model.PersistRdbEntry;

import java.util.List;

/**
 * rdb加载策略
 * 通过文件路径加载
 */
public class CacheLoadDbJson<K,V> implements ICacheLoad<K,V> {
    /**
     * 文件路径
     */
    private final String dbPath;

    public CacheLoadDbJson (String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtil.readAllLines(dbPath);
        if (CollectionUtil.isEmpty(lines)) {
            return;
        }
        for (String line : lines) {
            if (StringUtil.isEmpty(line)) {
                continue;
            }
            //反序列化
            PersistRdbEntry<K,V> entry = JSON.parseObject(line,PersistRdbEntry.class);
            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();

            cache.put(key,value);
            if (ObjectUtil.isNotNull(expire)) {
                cache.expireAt(key,expire);
            }

        }
    }
}
