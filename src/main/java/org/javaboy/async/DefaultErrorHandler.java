package org.javaboy.async;

import org.javaboy.async.context.AsynccallContext;

/**
 * @author:majin.wj
 */
public class DefaultErrorHandler implements ErrorHandler{
    @Override
    public void process(Throwable throwable, AsynccallContext context) {
        throw new RuntimeException(throwable);
    }
}
