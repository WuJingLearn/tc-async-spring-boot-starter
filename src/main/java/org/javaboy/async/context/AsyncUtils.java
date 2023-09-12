package org.javaboy.async.context;

import org.javaboy.async.context.AsynccallContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author:majin.wj
 */
public class AsyncUtils {


    private static ThreadLocal<AsynccallContext> context = new ThreadLocal<>();

    public static boolean isAsync() {
        return getContext() != null;
    }

    public static AsynccallContext getContext() {
        return context.get();
    }

    public static void setContext(AsynccallContext target) {
        context.set(target);
    }

    public static void removeContext() {
        context.remove();
        ;
    }

    public static String getSignature(Method method) {
        return method.getReturnType().getCanonicalName() + ":" + method.getName() + ":" + getParameterTypes(method);
    }


    public static String getParameterTypes(Method method) {
        return Arrays.asList(method.getParameterTypes()).stream()
                .map(p -> p.getCanonicalName())
                .collect(Collectors.joining(","));
    }


}
