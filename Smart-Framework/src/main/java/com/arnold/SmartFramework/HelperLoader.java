package com.arnold.SmartFramework;

import com.arnold.SmartFramework.helper.*;
import com.arnold.SmartFramework.util.ClassUtil;

public final class HelperLoader {
    public static void init(){
        //aop在ioc之前，首先aop替换类，ioc再依赖注入
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }

    }
}
