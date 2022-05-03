package com.ljx.myRedis.api;

/**
 * 慢日志操作接口
 * 每一个实现都可以定义自己的慢操作阈值，这样便于分级处理
 * 比如超过 100ms，用户可以选择输出 warn 日志；超过 1s，可以直接接入报警系统
 */
public interface ICacheSlowListener {
    /**
     * 监听
     * @param context 上下文
     */
    void listen (final ICacheSlowListenerContext context);

    /**
     * 慢日志的阈值
     * @return 慢日志的阈值
     */
    long slowerThanMills();
}
