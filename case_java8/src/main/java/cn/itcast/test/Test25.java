package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test25")
public class Test25 {
    // 表示 t2 是否运行过
    private static boolean t2runned = false;
    private final static ReentrantLock room = new ReentrantLock();
    private static Condition await = room.newCondition();
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            room.lock();
            try {
                while (!t2runned) {
                    log.debug("当前条件不满足");
                    try {
                        await.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                room.unlock();
            }
            log.debug("1");
        }, "t1");


        Thread t2 = new Thread(() -> {
            room.lock();
            try {
                log.debug("2");
                t2runned = true;
                await.signal();
            } finally {
                room.unlock();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
