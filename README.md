## 通用异步框架

### 介绍
spring的@Async注解，也是用来实现异步调用的；不过spring的@Async功能有一定的缺陷，比如无法重试，无法持久化任务，如果线程池中还有
任务未处理完成，此时服务重启，任务就全部丢失。

tc-async-call 通过MQ消息实现方法的异步调用;借助消息的持久化机制，以及消费失败重试机制，来提升异步方法执行的可靠性


### 功能原理
方法上加上`@Asynchronized`注解，那么该类就会被spring aop进行代理，在调用该方法时，会向RocketMq发送一条消息，
该方法就算执行完成； 消息的topic和消费组，用户可以自己指定，否则会使用默认的topic和消费组，默认的topic为async_${应用名}，消费组为async_${应用名}；
发送消息包含方法执行的参数,目标类，目标方法，
不同的消费组会创建不同的消费者来消费消息；消费者根据收到的消息，解析消息内容,然后反射调用方法来执行。



### 代码实列

```

    @Asynchronized(errorHandler = MyErrorHandler.class,resultHandler = NoOPResultHandler.class)
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
```