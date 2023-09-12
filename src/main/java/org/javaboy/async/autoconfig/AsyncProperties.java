package org.javaboy.async.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author:majin.wj
 */
@ConfigurationProperties(prefix = "async.call")
public class AsyncProperties implements EnvironmentAware {

    public static final String CLASS_NAME = "Class";
    public static final String METHOD_NAME = "MethodName";
    public static final String METHOD_SIGNATURE = "MethodSignature";

    public static final String TOPIC_NAME = "async_%s";
    public static final String CONSUMER_NAME = "async_%s";

    private String nameSrc;

    private String topic;

    private String tag;

    private String consumer;

    private Environment environment;


    public AsyncProperties() {
        String appName = environment.getProperty("app.name","unKnow");
        this.topic = String.format(TOPIC_NAME, appName);
        this.tag = "*";
        this.consumer = String.format(CONSUMER_NAME, appName);
    }


    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getConsumer() {
        return consumer;
    }


    public void setNameSrc(String nameSrc) {
        this.nameSrc = nameSrc;
    }

    public String getNameSrc() {
        return nameSrc;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
