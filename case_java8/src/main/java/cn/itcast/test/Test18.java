package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

@Slf4j(topic = "c.Test18")
public class Test18 {
    static int x;
    public static void main(String[] args) {
        Thread t2 = new Thread(()->{
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    log.debug("这是被打断后的线程");
                    System.out.println(x);
                    break;
                }
            }
        },"t2");
        t2.start();
        new Thread(()->{
            sleep(1);
            x = 10;
            t2.interrupt();
        },"t1").start();
        while(!t2.isInterrupted()) {
            Thread.yield();
        }
        System.out.println(x);
    }
}
