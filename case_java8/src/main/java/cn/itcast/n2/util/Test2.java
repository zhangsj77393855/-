package cn.itcast.n2.util;

import java.util.concurrent.TimeUnit;

public class Test2 {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> n1.a()).start();
        new Thread(() -> n1.b()).start();
    }
}

class Number{
    public  void a(){
        synchronized (this){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1");
        }
    }
    public synchronized void b() {
        System.out.println("2");
    }
}
