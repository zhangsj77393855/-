package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test27")
public class Test27 {
    public static void main(String[] args) {
        final WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()-> waitNotify.print("a",1,2),"输出a的").start();
        new Thread(()-> waitNotify.print("b",2,3),"输出b的").start();
        new Thread(()-> waitNotify.print("c",3,1),"输出c的").start();
    }
}

/*
输出内容       等待标记     下一个标记
   a           1             2
   b           2             3
   c           3             1
 */
class WaitNotify {

    // 等待标记
    private int flag; // 2
    // 循环次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    // 打印               a                          1                       2
    public void print(final String print,final int waitFlag,final int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while(flag != waitFlag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.flag = nextFlag;
                System.out.println(Thread.currentThread().getName() + "输出的是"+ print);
                this.notifyAll();
            }
        }
    }

    public void print1(final String print,final int waitFlag, final int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while(flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}
