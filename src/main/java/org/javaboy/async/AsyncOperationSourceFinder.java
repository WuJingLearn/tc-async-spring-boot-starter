package org.javaboy.async;

import org.apache.commons.lang3.StringUtils;
import org.javaboy.async.annotation.Asynchronized;
import org.javaboy.async.autoconfig.AsyncProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

/**
 * @author:majin.wj
 */
public class AsyncOperationSourceFinder implements BeanPostProcessor {


    @Autowired
    private AsyncProperties properties;

    @Autowired
    private AsyncMessageListenerManager messageListenerManager;

    @Autowired
    private AsyncOperationSource asyncOperationSource;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithMethods(bean.getClass(), m -> {
            Asynchronized annotation = m.getAnnotation(Asynchronized.class);
            if (annotation != null) {
                try {
                    asyncOperationSource.computeAsyncOperation(m, bean.getClass());
                    registerMessageListener(annotation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return bean;
    }

    private void registerMessageListener(Asynchronized asynchronized) {
        String topic = StringUtils.isEmpty(asynchronized.topic()) ? properties.getTopic() : asynchronized.topic();
        String tag = asynchronized.tag();
        String consumer = StringUtils.isEmpty(asynchronized.consumer()) ? properties.getConsumer() : asynchronized.consumer();

        messageListenerManager.registerIfNecessary(topic, tag, consumer);
    }


}
