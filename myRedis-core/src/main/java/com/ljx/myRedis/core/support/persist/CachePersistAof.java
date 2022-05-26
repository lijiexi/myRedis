package com.ljx.myRedis.core.support.persist;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于AOF的持久化
 */
public class CachePersistAof<K,V> extends CachePersistAdaptor<K,V> {

    private static final Log log = LogFactory.getLog(CachePersistAof.class);

    /**
     * 缓存列表
     */
    private final List<String> bufferLsit = new ArrayList<>();

    /**
     * 数据持久化路径
     */
    private final String dbPath;

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void persist(ICache<K, V> cache) {
        //log.info("开始AOF持久化到文件");
        //1.创建文件
        if (!FileUtil.exists(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        //2.将buffer文件，持久化追加到文件中
        FileUtil.append(dbPath,bufferLsit);
        //3.清空buffer
        bufferLsit.clear();
        log.info("完成AOF持久化到文件");
    }

    @Override
    public long delay() {
        return 10;
    }

    @Override
    public long period() {
        return 10;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }

    /**
     * 添加json信息到buffer列表中
     * @param json json信息
     */
    public void append (final String json) {
        if (StringUtil.isNotEmpty(json)) {
            bufferLsit.add(json);
        }
    }
}
