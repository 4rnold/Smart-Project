package com.arnold.SmartFramework.helper;

import com.arnold.SmartFramework.annotation.Aspect;
import com.arnold.SmartFramework.proxy.AspectProxy;
import com.arnold.SmartFramework.proxy.Proxy;
import com.arnold.SmartFramework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

public class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                //创建代理类
                Object proxy = ProxyManager.createProxy(targetEntry.getKey(), targetEntry.getValue());
                //将容器中的class 换为 代理类，因为hashmap 所以会覆盖 key的value
                BeanHelper.setBean(targetEntry.getKey(), proxy);

            }
        } catch (Exception e) {
            LOGGER.error("aop fail",e);
        }

    }



    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 找到继承AspectProxy的代理类
     * 根据代理类的Aspect注解中value 找到被代理类
     * 返回代理类和被代理类的映射
     * proxyClass -> set<targetClass>
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();

        //切面类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyCls : proxyClassSet) {
            if (proxyCls.isAnnotationPresent(Aspect.class)){//继承了AspectProxy并且被Aspect标注
                Aspect aspect = proxyCls.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyCls, targetClassSet);
            }
        }
        return proxyMap;
    }

    /**
     * targetClass -> List<proxy instance>
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        HashMap<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }
}
