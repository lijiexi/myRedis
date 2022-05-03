package com.ljx.myRedis.core.support.proxy.dynamic;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.core.support.proxy.CacheProxy;
import com.ljx.myRedis.core.support.proxy.ICacheProxy;
import com.ljx.myRedis.core.support.proxy.bs.CacheProxyBs;
import com.ljx.myRedis.core.support.proxy.bs.CacheProxyBsContext;
import com.ljx.myRedis.core.support.proxy.bs.ICacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理
 */
public class DynamicProxy implements InvocationHandler, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;
    public DynamicProxy(ICache target) {
        this.target = target;
    }
    /**
     * 调用原生方法（被代理类的方法）并自定义一些处理逻辑
     * 动态代理对象调用原生方法的时候，最终实际上调用到的是 invoke() 方法，
     * 然后 invoke() 方法去调用了被代理对象的原生方法。
     * @param proxy 动态生成的代理类
     * @param method 与代理类对象调用的方法相对应
     * @param args  当前 method 方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(args).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }
    @Override
    public Object proxy() {
        //代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new DynamicProxy(target);
        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                target.getClass().getInterfaces(),handler);
    }
}
