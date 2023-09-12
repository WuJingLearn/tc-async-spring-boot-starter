package org.javaboy.async.aspect;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.google.common.base.Preconditions;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javaboy.async.AsyncOperation;
import org.javaboy.async.AsyncOperationSource;
import org.javaboy.async.autoconfig.AsyncProperties;
import org.javaboy.async.context.AsyncUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @author:majin.wj
 */
public class AsyncMethodInterceptor extends AsyncAspectSupport implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMethodInterceptor.class);

    @Autowired
    private AsyncOperationSource operationSource;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (AsyncUtils.isAsync()) {
            return invocation.proceed();
        }

        Object target = invocation.getThis();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        Class<?> targetClass = getTargetClass(target);

        AsyncOperation asyncOperation = operationSource.getAsyncOperation(method, targetClass);
        if (asyncOperation == null) {
            return invocation.proceed();
        }

        Message message = new Message(asyncOperation.getTopic(), asyncOperation.getTag(), asyncOperation.getSerializingConverter().convert(args));
        message.putUserProperty(AsyncProperties.CLASS_NAME,targetClass.getName());
        message.putUserProperty(AsyncProperties.METHOD_NAME, method.getName());
        message.putUserProperty(AsyncProperties.METHOD_SIGNATURE, asyncOperation.getSignature());
        // 基础组件的可用性是很高的。
        SendResult sendResult = rocketProducer.send(message);
        LOG.info("AsyncMethodInterceptor:messageId=" + sendResult.getMsgId() + ",sendResult:" + sendResult + ",signature=" + asyncOperation.getSignature());
        Preconditions.checkState(sendResult.getSendStatus() == SendStatus.SEND_OK);
        return null;
    }
}
