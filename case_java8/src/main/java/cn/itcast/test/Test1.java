package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test1")
public class Test1 {

    public static void test2() {

        Thread t = new Thread(()-> log.debug("running"), "t2");
        t.start();
    }
    public static void test1() {
        Thread t = new Thread(() -> log.debug("running"));
        t.setName("t1");
        t.start();

    }

    public static void main(String[] args) {
        Thread thread = new Thread(() -> log.debug("running"));
        thread.setName("222");
        thread.start();

        Runnable runnable = () -> log.debug("执行任务");
        final Thread thread1 = new Thread(runnable,"t");
        thread1.start();
    }
}
