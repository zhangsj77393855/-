package cn.itcast.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test30 {
    public static void main(String[] args) throws InterruptedException {
        final AwaitSignal awaitSignal = new AwaitSignal(500);
        final Condition a = awaitSignal.newCondition();
        final Condition b = awaitSignal.newCondition();
        final Condition c = awaitSignal.newCondition();
        new Thread(()-> awaitSignal.print("a", a, b),"输出a").start();
        new Thread(()-> awaitSignal.print("b", b, c),"输出b").start();
        new Thread(()-> awaitSignal.print("c", c, a),"输出c").start();

        Thread.sleep(1000);
        awaitSignal.lock();
        try{
            System.out.println("唤醒a线程,从a开始执行");
            a.signal();
        }finally {
            awaitSignal.unlock();
        }


    }
}

class AwaitSignal extends ReentrantLock{
    private final int loopNumber;

    public AwaitSignal(final int loopNumber) {
        this.loopNumber = loopNumber;
    }
    //            参数1 打印内容， 参数2 进入哪一间休息室, 参数3 下一间休息室
    public void print(final String print,final Condition current,final Condition next) {
        try {
            this.lock();
            for (int i = 0; i < loopNumber; i++) {
                try {
                    current.await();
                    next.signal();
                    System.out.println(Thread.currentThread().getName() + "输出的是"+ print);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            unlock();
        }
    }
}
