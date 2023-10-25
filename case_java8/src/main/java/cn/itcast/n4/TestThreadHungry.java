package cn.itcast.n4;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.TestParkUnpark")
public class TestThreadHungry {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                while(true){
                    log.debug("1111111");
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

        Thread.sleep(1000);
        Thread me = new Thread(() -> {
            System.out.println("done");
        });
        me.setPriority(Thread.MIN_PRIORITY);
        me.start();
    }
}
