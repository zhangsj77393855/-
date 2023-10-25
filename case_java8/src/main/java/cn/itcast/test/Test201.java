package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "c.Test201")
public class Test201 {
    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
         new Thread(new People2()).start();
        }

        Sleeper.sleep(1);
        for (Integer id: Mailboxes2.getIds()) {
            new Thread(new Postman2(id,"内容" + id)).start();
        }
    }
}
@Slf4j(topic = "c.People2")
class People2 implements Runnable {

    @Override
    public void run() {
        // 创建收信箱
        final GuardedObject2 guardedObject2 = Mailboxes2.cerateGuardedObject2();
        log.debug("开始收信 id:{}", guardedObject2.getId());
        final Object response = guardedObject2.getResponse(5000);
        log.debug("收到信 id:{}, 内容:{}", guardedObject2.getId(), response);
    }
}

@Slf4j(topic = "c.Postman2")
class Postman2 implements Runnable {
    private int id;
    private String mail;

    public Postman2(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        final GuardedObject2 guardedObject2 = Mailboxes2.getGuardedObject2(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject2.complete(mail);
    }
}

class Mailboxes2 {
    public static final Map<Integer, GuardedObject2> boxs = new ConcurrentHashMap<>();

    public static int id = 1;

    // 产生唯一的id
    public static synchronized int generateId () {
        return id++;
    }
    public static GuardedObject2 getGuardedObject2(final int id) {
        return boxs.remove(id);
    }

    public static GuardedObject2 cerateGuardedObject2(){
        final GuardedObject2 guardedObject2 = new GuardedObject2(generateId());
        boxs.put(guardedObject2.getId(), guardedObject2);
        return guardedObject2;
    }

    public static Set<Integer> getIds() {
        return boxs.keySet();
    }

}

class GuardedObject2{
    private int id;

    public GuardedObject2(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 结果
    private Object response;

    // 获取结果
    public Object getResponse(final long timeout) {
        synchronized (this) {
            // 开始时间
            final long beginTime = System.currentTimeMillis();
            // 经历的时间
            long passTime = 0L;
            while(response == null) {
                // 当前剩余的等待时间
                final long waitTime = timeout - passTime;
                // 当前剩余的时间小于等于0时退出循环
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {

                }
                // 求出来经历的时间
                passTime = System.currentTimeMillis() - beginTime;
            }
            return response;
        }
    }

    // 产生结果
    public void complete (final Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
