package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @Author：zsj
 * @Date：2023/8/3
 */
public class TestWaitQeue {
    public static void main(String[] args) {
        final MessageQueue1 messageQueue1 = new MessageQueue1(3);
        for (int i = 0; i < 10; i++) {
            int id = i;
            new Thread(() ->{
                final Message1 message1 = new Message1(id, "值" + id);
                messageQueue1.put(message1);
            },"生产者" + i).start();
        }
        new Thread(()->{
            while(true) {
                try {
                    Thread.currentThread().sleep(1000);
                    messageQueue1.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者").start();
    }
}
// 消息队列类 ， java 线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue1 {
    // 消息的队列集合
    private LinkedList<Message1> list = new LinkedList<>();
    // 队列容量
    private int capcity;

    public MessageQueue1(int capcity) {
        this.capcity = capcity;
    }

    // 获取消息
    public Message1 take() {
        synchronized (list) {
            while(list.isEmpty()) {
                try {
                    log.debug("目前队列里面没有数据");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final Message1 message = list.removeFirst();
            log.debug("消费者消费数据:{}",message);
            list.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message1 message) {
        synchronized (list) {
            while(list.size() == capcity) {
                try {
                    log.debug("目前队列里面数据满了");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);
            log.debug("生产者存入信息:{}",message);
            list.notifyAll();
        }
    }
}
final class Message1 {
    private int id;
    private Object value;

    public Message1(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
