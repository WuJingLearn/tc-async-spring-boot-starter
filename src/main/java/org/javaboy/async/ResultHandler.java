package org.javaboy.async;

import org.javaboy.async.context.AsynccallContext;

/**
 * @author:majin.wj
 */
public interface ResultHandler <T> {
    void process(T result, AsynccallContext context);
}
