package org.javaboy.async;

import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

/**
 * @author:majin.wj
 */
public class AsyncOperation {

    private String topic;
    private String tag;
    private String signature;
    private SerializingConverter serializingConverter;
    private DeserializingConverter deserializingConverter;
    private ResultHandler resultHandler;
    private ErrorHandler errorHandler;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }


    public void setSerializingConverter(SerializingConverter serializingConverter) {
        this.serializingConverter = serializingConverter;
    }

    public SerializingConverter getSerializingConverter() {
        return serializingConverter;
    }

    public void setDeserializingConverter(DeserializingConverter deserializingConverter) {
        this.deserializingConverter = deserializingConverter;
    }

    public DeserializingConverter getDeserializingConverter() {
        return deserializingConverter;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
