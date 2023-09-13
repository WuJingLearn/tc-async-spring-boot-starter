package org.javaboy.async.autoconfig;

import org.javaboy.async.AsyncMessageListenerManager;
import org.javaboy.async.AsyncOperationSource;
import org.javaboy.async.AsyncOperationSourceFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:majin.wj
 */
@ConditionalOnProperty(name = "async.call.nameSrc")
@EnableConfigurationProperties(value = {AsyncProperties.class})
@Configuration
public class AsyncAutoConfiguration {

    @Bean
    public AsyncMessageListenerManager messageListenerManager() {
        return new AsyncMessageListenerManager();
    }

    @Bean
    public AsyncOperationSourceFinder operationSourceFinder() {
        return new AsyncOperationSourceFinder();
    }

    @Bean
    public AsyncOperationSource asyncOperationSource() {
        return new AsyncOperationSource();
    }

}
