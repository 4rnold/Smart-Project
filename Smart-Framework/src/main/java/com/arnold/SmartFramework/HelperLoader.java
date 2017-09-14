package com.arnold.SmartFramework;

import com.arnold.SmartFramework.helper.BeanHelper;
import com.arnold.SmartFramework.helper.ClassHelper;
import com.arnold.SmartFramework.helper.ControllerHelper;
import com.arnold.SmartFramework.helper.IocHelper;
import com.arnold.SmartFramework.util.ClassUtil;

public final class HelperLoader {
    public static void init(){
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, IocHelper.class, ControllerHelper.class};
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }

    }
}
