package cn.itcast.n2;

import java.util.concurrent.TimeUnit;

public class Test3 {
    public static void main(String[] args) {
        Number1 n1 = new Number1();
        new Thread(() -> n1.a()).start();
        new Thread(() -> n1.b()).start();
        new Thread(() -> n1.c()).start();
    }
}

class Number1{
    public synchronized void a() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1");;
    }
    public synchronized void b() {
        System.out.println("2");;
    }
    public void c() {
        System.out.println("3");
    }
}
