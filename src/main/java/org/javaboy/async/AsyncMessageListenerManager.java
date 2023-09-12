package org.javaboy.async;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:majin.wj
 */
public class AsyncMessageListenerManager implements DisposableBean, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired
    private AsyncOperationSource asyncOperationSource;

    private Map<String, DefaultMQPushConsumer> messageListeners = new HashMap<>();


    public void registerIfNecessary(String topic, String tag, String consumerId) {

        if (!messageListeners.containsKey(consumerId)) {
            try {
                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
                consumer.subscribe(topic, tag);
                consumer.registerMessageListener(new AsyncMessageListener(beanFactory,asyncOperationSource));
                consumer.start();
            } catch (Exception e) {
                throw new RuntimeException("rocketMq启动失败", e);
            }
        }

    }


    @Override
    public void destroy() throws Exception {

        for (DefaultMQPushConsumer consumer : messageListeners.values()) {
            consumer.shutdown();
        }

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
