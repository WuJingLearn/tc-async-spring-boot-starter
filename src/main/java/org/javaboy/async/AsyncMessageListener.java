package org.javaboy.async;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.javaboy.async.annotation.Asynchronized;
import org.javaboy.async.autoconfig.AsyncProperties;
import org.javaboy.async.context.AsyncUtils;
import org.javaboy.async.context.AsynccallContext;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author:majin.wj
 */
public class AsyncMessageListener implements MessageListenerConcurrently {

    private BeanFactory beanFactory;

    private AsyncOperationSource asyncOperationSource;

    public AsyncMessageListener(BeanFactory beanFactory, AsyncOperationSource asyncOperationSource) {
        this.beanFactory = beanFactory;
        this.asyncOperationSource = asyncOperationSource;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        MessageExt message = list.get(0);
        try {
            AsynccallContext asynccallContext = new AsynccallContext();
            AsyncUtils.setContext(asynccallContext);
            Class<?> targetClass = getTargetClass(message);
            Method method = getTargetMethod(message, targetClass);
            AsyncOperation asyncOperation = asyncOperationSource.getAsyncOperation(method, targetClass);
            Object target = beanFactory.getBean(targetClass);
            Object[] args = (Object[]) asyncOperation.getDeserializingConverter().convert(message.getBody());

            asynccallContext.setTarget(target);
            asynccallContext.setTargetMethod(method);
            asynccallContext.setArgs(args);
            asynccallContext.setAsyncOperation(asyncOperation);

            asynccallContext.invoke();
        } catch (Throwable ex) {
            Throwable throwable = ex;
            if(throwable instanceof InvocationTargetException) {
                throwable = Optional.ofNullable(((InvocationTargetException) throwable).getTargetException()).orElse(throwable);
            }
            AsynccallContext ctx = AsyncUtils.getContext();
            // 交给业务处理
            ctx.getErrorHandler().process(throwable, ctx);
        }finally {
            AsyncUtils.removeContext();
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
    public Class<?> getTargetClass(MessageExt messageExt) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(messageExt.getUserProperty(AsyncProperties.CLASS_NAME));
    }

    public Method getTargetMethod(MessageExt messageExt, Class<?> targetClass) {
        Method[] candidates = MethodUtils.getMethodsWithAnnotation(targetClass, Asynchronized.class);
        if (candidates == null || candidates.length < 1) {
            return null;
        }

        String signature = messageExt.getUserProperty(AsyncProperties.METHOD_SIGNATURE);

        for (Method candidate : candidates) {
            if (signature.equals(AsyncUtils.getSignature(candidate))) {
                return candidate;
            }
        }

        return null;
    }
}
