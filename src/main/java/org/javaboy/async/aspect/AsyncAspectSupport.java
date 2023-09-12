package org.javaboy.async.aspect;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.javaboy.async.autoconfig.AsyncProperties;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author:majin.wj
 */
public abstract class AsyncAspectSupport implements InitializingBean, DisposableBean {


    protected DefaultMQProducer rocketProducer;

    @Autowired
    private AsyncProperties properties;

    public Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rocketProducer = new DefaultMQProducer("async-producer");
        rocketProducer.setNamesrvAddr(properties.getNameSrc());
        rocketProducer.start();
    }

    @Override
    public void destroy() throws Exception {
        rocketProducer.shutdown();
    }

}
