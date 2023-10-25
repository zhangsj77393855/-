package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("1111");
                Thread.sleep(8500);
                return 1;
            }
        });
        final Thread thread = new Thread(futureTask,"444");
        thread.start();

        // 当我们在开启线程执行一系列的操作后,我们此时需要拿到前面的futureTask的结果时,可以直接
        // 这里会阻塞在这里一直等着方法的线程的执行结束

        log.debug("现在开始阻塞,等待异步线程的执行结束");
        final Integer integer = futureTask.get();
        log.debug("{}",integer);
        log.debug("开始下面的执行");
    }
}
