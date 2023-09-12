package org.javaboy.async;

import org.apache.commons.lang3.StringUtils;
import org.javaboy.async.annotation.Asynchronized;
import org.javaboy.async.autoconfig.AsyncProperties;
import org.javaboy.async.context.AsyncUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodClassKey;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:majin.wj
 */
public class AsyncOperationSource {


    private Map<Object, AsyncOperation> asyncOperationCache = new ConcurrentHashMap<>(1024);

    @Autowired
    private AsyncProperties properties;

    public AsyncOperation getAsyncOperation(Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = getCacheKey(method, targetClass);
        AsyncOperation asyncOperation = asyncOperationCache.get(cacheKey);
        if (asyncOperation == null) {
            asyncOperation = computeAsyncOperation(method, targetClass);
            asyncOperationCache.put(cacheKey, asyncOperation);
        }
        return asyncOperation;
    }

    public AsyncOperation computeAsyncOperation(Method method, Class<?> targetClass) {
        try {
            Asynchronized annotation = method.getAnnotation(Asynchronized.class);
            AsyncOperation asyncOperation = new AsyncOperation();
            asyncOperation.setTopic(StringUtils.isEmpty(annotation.topic()) ? properties.getTopic() : annotation.topic());
            asyncOperation.setTag(annotation.tag());
            asyncOperation.setSignature(AsyncUtils.getSignature(method));
            asyncOperation.setSerializingConverter(new SerializingConverter(annotation.serializer().newInstance()));
            asyncOperation.setDeserializingConverter(new DeserializingConverter(annotation.deserializer().newInstance()));
            asyncOperation.setResultHandler(annotation.resultHandler().newInstance());
            asyncOperation.setErrorHandler(annotation.errorHandler().newInstance());
            return asyncOperation;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getCacheKey(Method method, Class<?> clazz) {
        return new MethodClassKey(method, clazz);
    }


}
