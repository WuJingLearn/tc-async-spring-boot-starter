package org.javaboy.async;

import org.javaboy.async.context.AsynccallContext;

/**
 * @author:majin.wj
 */
public interface ErrorHandler {
    void process(Throwable throwable, AsynccallContext context);

}
