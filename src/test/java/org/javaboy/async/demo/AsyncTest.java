package org.javaboy.async.demo;

import org.javaboy.async.ErrorHandler;
import org.javaboy.async.annotation.Asynchronized;
import org.javaboy.async.context.AsynccallContext;

/**
 * @author:majin.wj
 */
public class AsyncTest {


    @Asynchronized(errorHandler = MyErrorHandler.class)
    public String m1(String arg1) {

        System.out.println("test start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("test end");
        return "succes";
    }

    static class MyErrorHandler implements ErrorHandler {

        @Override
        public void process(Throwable throwable, AsynccallContext context) {
            if (context.getRetryCount() > 3) {
                System.out.println("超过三次不重试了");
                return;
            }
            throw new RuntimeException("");
        }
    }

}
