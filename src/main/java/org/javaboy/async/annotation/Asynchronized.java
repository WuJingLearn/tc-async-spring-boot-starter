package org.javaboy.async.annotation;

import org.javaboy.async.DefaultErrorHandler;
import org.javaboy.async.ErrorHandler;
import org.javaboy.async.NoOPResultHandler;
import org.javaboy.async.ResultHandler;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.lang.annotation.*;

/**
 * @author:majin.wj
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Asynchronized {


    String topic() default "";

    String tag() default "*";

    String consumer() default "";


    Class<? extends Serializer> serializer() default DefaultSerializer.class;

    Class<? extends Deserializer> deserializer() default DefaultDeserializer.class;

    /**
     * 异常处理器
     * @return
     */
    Class<? extends ErrorHandler> errorHandler() default DefaultErrorHandler.class;

    /**
     * 结果处理器
     * @return
     */
    Class<? extends ResultHandler> resultHandler() default NoOPResultHandler.class;

}
