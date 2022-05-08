package com.ljx.myRedis.core.support.persist;

import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICachePersist;
import com.ljx.myRedis.core.model.PersistRdbEntry;

import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于 json 的持久化
 * 缓存持久化 db
 */
public class CachePersistDbJson<K, V> implements ICachePersist<K, V> {
    /**
     * 数据库路径
     */
    private final String dbPath;

    public CachePersistDbJson (String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * Json持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K, V>> entrySet = cache.entrySet();
        //创建文件
        FileUtil.createFile(dbPath);
        //清空文件,重新rdb
        FileUtil.truncate(dbPath);

        for (Map.Entry<K, V> entry : entrySet) {
            K key = entry.getKey();
            //获取该key对应的过期时间
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K, V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(entry.getValue());
            persistRdbEntry.setExpire(expireTime);
            String line = JSON.toJSONString(persistRdbEntry);
            FileUtil.write(dbPath,line,StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
