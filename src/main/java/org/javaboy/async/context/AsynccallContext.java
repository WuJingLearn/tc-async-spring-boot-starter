package org.javaboy.async.context;

import org.javaboy.async.AsyncOperation;
import org.javaboy.async.ErrorHandler;

import java.lang.reflect.Method;

/**
 * @author:majin.wj
 */
public class AsynccallContext {

    private int retryCount;
    private Method targetMethod;
    private Object target;
    private Object[] args;

    private AsyncOperation asyncOperation;


    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setAsyncOperation(AsyncOperation asyncOperation) {
        this.asyncOperation = asyncOperation;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArgs() {
        return args;
    }

    public AsyncOperation getAsyncOperation() {
        return asyncOperation;
    }

    public ErrorHandler getErrorHandler() {
        return asyncOperation.getErrorHandler();
    }

    public void invoke() throws Throwable {
        if (targetMethod.getReturnType() == void.class || targetMethod.getReturnType() == Void.class) {
            targetMethod.invoke(target, args);
        } else {
            Object result = targetMethod.invoke(target, args);
            if (asyncOperation.getResultHandler() != null) {
                asyncOperation.getResultHandler().process(result, this);
            }
        }
    }
}
