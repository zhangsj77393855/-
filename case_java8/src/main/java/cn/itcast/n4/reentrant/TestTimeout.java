package cn.itcast.n4.reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static cn.itcast.n2.util.Sleeper.sleep;

@Slf4j(topic = "c.TestTimeout")
public class TestTimeout {
    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取等待 1s 后失败，返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("现在被打断了,查看一下当前线程的状态:{}", Thread.currentThread().getState());
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(2);
            t1.interrupt();
        } finally {
            lock.unlock();
        }
    }
    private static void test2() {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            if (!lock.tryLock()) {
                log.debug("获取立刻失败，返回");
                return;
            }
            try {
                log.debug("t1线程获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(2);
        } finally {
            lock.unlock();
        }
    }
}
