package com.arnold.SmartFramework.proxy;

import com.arnold.SmartFramework.annotation.Transaction;
import com.arnold.SmartFramework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);


    //保证同一线程中事务控制相关逻辑只会执行一次?
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Exception, Throwable {
        Boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        Object result = null;
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            } finally {
                //DatabaseHelper.closeConnetion();
                FLAG_HOLDER.remove();
            }

        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
