package com.arnold.SmartFramework.proxy;

public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Exception, Throwable;
}
