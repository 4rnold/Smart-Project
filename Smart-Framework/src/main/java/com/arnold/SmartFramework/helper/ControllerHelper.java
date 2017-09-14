package com.arnold.SmartFramework.helper;

import com.arnold.SmartFramework.Bean.Handler;
import com.arnold.SmartFramework.Bean.Request;
import com.arnold.SmartFramework.annotation.Action;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {

    private static final Map<Request,Handler> HANDLER_MAP = new HashMap<>();

    static {
        //从@action注解中解析 request 对应关系  handler = controller + method
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (!controllerClassSet.isEmpty()) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] controllerMethods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(controllerMethods)) {
                    for (Method controllerMethod : controllerMethods) {
                        if (controllerMethod.isAnnotationPresent(Action.class)) {
                            Action action = controllerMethod.getAnnotation(Action.class);
                            String value = action.value();
                            if (value.matches("\\w+:/\\w*")) {
                                String[] array = value.split(":");
                                if (array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestPath, requestMethod);
                                    Handler handler = new Handler(controllerClass, controllerMethod);
                                    HANDLER_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestPath, requestMethod);
        return HANDLER_MAP.get(request);
    }
}
