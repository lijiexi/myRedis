# 项目简介

[myRedis](https://github.com/lijiexi/myRedis)用于实现一个可扩展的仿Redis的本地缓存。

同时实现可与本项目在线交互的网站，使用本项目中Java实现的Redis。

**在线演示网站**:http://redis.lijiexi.com/

## 项目意义

* 为日常开发提供一套简单易用的缓存框架。
* 学以致用，根据Redis使用Java语言开发一个本地的缓存框架。

## myRedis特性

* 使用fluent流式编程，提高编程体验
* 支持**指定容量**的缓存
* 支持自定义Map实现策略
* 支持**Expire过期特性**
* 支持**自定义Evict过期驱逐策略**
* 支持**定时删除和过期删除**
* 内置**FIFO、LRU、LRU-2等驱逐策略**
* 支持**自定义删除监听器**
* 日志整合框架，自适应常见日志
* 支持 **Load 初始化和 Persist 持久化**
* 支持**RDB和AOF**两种持久化模式
* 支持**慢操作监听**

# 安装&使用

## 环境

JDK1.7及以上版本

导入本地myRedis.Jar包

## 初始化测试

```java
    @Test
    public void helloTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(2)
                .build();
        cache.put("1","1");
        cache.put("2","2");
        cache.put("3","3");
        System.out.println(cache.keySet());
        Assert.assertEquals(2, cache.size());
    }
```

默认使用FIFO淘汰策略，此时输出如下：

```
[2, 3]
```

上述配置等价于：

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .map(Maps.<String,String>hashMap())
                .evict(CacheEvicts.<String, String>fifo())
                .size(2)
                .build();
```

## 淘汰策略

目前实现了以下几种淘汰策略，可以通过CacheEvicts选择。

| 策略             | 说明                                          |
| ---------------- | --------------------------------------------- |
| none             | 没有任何淘汰策略                              |
| fifo             | 先进先出（默认策略）                          |
| lru              | 最基本的朴素 LRU 策略，性能一般               |
| lruDoubleListMap | 基于双向链表+MAP 实现的朴素 LRU，性能优于 lru |
| lru2             | 基于 LRU-2 的改进版 LRU 实现，命中率优于 lru  |

## 过期实现

### 定时删除

使用线程池实现定时删除

### 惰性删除

使用动态代理实现惰性删除

```java
    @Test
    public void expireTest () throws InterruptedException {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).build();
        cache.put("2","2");
        cache.put("3","3");
        cache.put("4","4");
        TimeUnit.MILLISECONDS.sleep(50);
        System.out.println(cache.keySet());
        cache.expire("2",40);
        Thread.sleep(50);
        System.out.println(cache.keySet());
        Assert.assertEquals(2, cache.size());
    }
```

`cache.expire("2", 40);` 指定对应的 key 在 40ms 后过期。

## 删除监听器

在两种场景下删除数据是对用户透明的：

（1）size 满了之后，进行数据淘汰。

（2）expire 过期时，清除数据。

如果用户关心的话，可以继承`ICacheRemoveListener`接口自定义删除监听器。

### 使用

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        .size(1)
        .addRemoveListener(new MyRemoveListener<String, String>())
        .build();

cache.put("1", "1");
cache.put("2", "2");
```

### 测试日志

```
Remove key: 2, value: 2, type: expire
```

## 添加慢操作监听器

### 说明

redis 中会存储慢操作的相关日志信息，主要是由两个参数构成：

（1）slowlog-log-slower-than 预设阈值,它的单位是毫秒(1秒=1000000微秒)默认值是10000

（2）slowlog-max-len 最多存储多少条的慢日志记录

### 使用

```java
    @Test
    public void slowListenerTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).addSlowListener(new MySlowListener())
                .addSlowListener(CacheSlowListeners.defaults()).build();
        cache.put("1","1");
        cache.get("1");
        cache.get("2");
    }
```

### 测试日志

```
【慢日志】name: put
[WARN] [2022-06-02 17:12:21.627] [main] [c.l.m.c.s.i.c.CacheInterceptorCost.listen] - [Slow] methodName: put, params: ["1","1"], cost time: 2
【慢日志】name: get
[WARN] [2022-06-02 17:12:21.628] [main] [c.l.m.c.s.i.c.CacheInterceptorCost.listen] - [Slow] methodName: get, params: ["1"], cost time: 0
【慢日志】name: get
[WARN] [2022-06-02 17:12:21.629] [main] [c.l.m.c.s.i.c.CacheInterceptorCost.listen] - [Slow] methodName: get, params: ["2"], cost time: 0
```

### Load加载器

**支持RDB和AOF两种格式加载数据**。

### RDB加载测试

```java
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).load(CacheLoads.dbJson("rdb.txt"))
                .persist(CachePersists.dbJson("rdb.txt"))
                .build();
```

### AOF加载测试

```java
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).load(CacheLoads.aof("aof.txt")).build();
        System.out.println(cache.entrySet());
```

# 添加 persist 持久化类

使用RDB或者AOF的方式对缓存持久化，使这些 key/value 信息可以持久化，存储到文件或者 database 中。

### RDB持久化

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/%E6%88%AA%E5%B1%8F2022-06-02%2017.23.39.png)

### AOF持久化

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/%E6%88%AA%E5%B1%8F2022-06-02%2017.19.36.png)

### 使用

```java
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .load(new MyCacheLoad())
                .persist(CachePersists.dbJson("test.rdb")).build();
```





