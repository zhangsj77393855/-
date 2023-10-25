package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static cn.itcast.n2.util.Sleeper.sleep;

@Slf4j(topic = "c.Test24")
public class Test24 {
    static boolean hasCigarette = false; // 有没有烟
    static boolean hasTakeout = false;
    static ReentrantLock room = new ReentrantLock();
    static Condition smoke = room.newCondition();
    static Condition takeOut = room.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            room.lock();
            try {
                log.debug("有烟没？[{}]", hasCigarette);
                while(!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        smoke.await();
                    } catch (InterruptedException e) {
                        log.debug("没有获取到锁");
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                room.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            room.lock();
            try {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while(!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        takeOut.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                room.unlock();
            }
        }, "小女").start();
        sleep(1);
        new Thread(() -> {
            room.lock();
            try {
                hasCigarette = true;
                smoke.signal();
                log.debug("烟到了噢！");
            } finally {
                room.unlock();
            }
        }, "送烟的").start();

        sleep(1);
        new Thread(() -> {
            room.lock();
            try {
                hasTakeout = true;
                takeOut.signal();
                log.debug("外卖到了噢！");
            } finally {
                room.unlock();
            }
        }, "送外卖的").start();
    }
}

