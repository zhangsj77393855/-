package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @Author：zsj
 * @Date：2023/8/3
 */
@Slf4j(topic = "c.Test20")
public class Test8888 {
    public static void main(String[] args) {
//        final GuardedObject1 guardedObject1 = new GuardedObject1(1);
//        new Thread(() -> {final Object o = guardedObject1.get(5000);
//        log.debug("{}", o);},"1111").start();
//        new Thread(() -> {
//            try {
//                Thread.currentThread().sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            guardedObject1.complete(2000);
//        },"2222").start();
        for (int i = 0; i < 8; i++) {
            new People1().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes1.getIds()) {
            new Postman1(id, "mail的id" + id).start();
        }
    }
}


@Slf4j(topic = "c.People")
class People1 extends Thread{
    @Override
    public void run() {
        // 收信
        final GuardedObject1 guardedObject = Mailboxes1.createGuardedObject();
        final int id = guardedObject.getId();
        log.debug("此次信箱的id是:{}",id);
        final Object o = guardedObject.get(5000);
        log.debug("此次收信的id是:{} 信件内容是:{}",id,o);
    }
}

@Slf4j(topic = "c.Postman1")
class Postman1 extends Thread {
    private int id;
    private String mail;

    public Postman1(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        final GuardedObject1 guardedObject = Mailboxes1.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}

class Mailboxes1 {
    private static Map<Integer,GuardedObject1> boxs= new Hashtable<>();

    private static int id = 1;

    private static synchronized int getId() {
        return id++;
    }

    public static GuardedObject1 getGuardedObject(int id) {
        return boxs.get(id);
    }

    public static GuardedObject1 createGuardedObject() {
        GuardedObject1 go = new GuardedObject1(getId());
        boxs.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxs.keySet();
    }
}
// 增加超时效果
class GuardedObject1 {

    // 标识 Guarded Object
    private int id;

    public GuardedObject1(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 结果
    private Object response;

    // 获取结果
    // timeout 表示要等待多久 2000
    public Object get(long timeout) {
        synchronized (this){
            final long beginTime = System.currentTimeMillis();
            long passTime = 0;
            while(response == null) {
                long waitTime = timeout - passTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                }
                passTime = System.currentTimeMillis() - beginTime;
            }
            return response;

        }
    }

    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
