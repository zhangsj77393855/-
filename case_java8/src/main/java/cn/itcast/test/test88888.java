package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author：zsj
 * @Date：2023/8/10
 */
@Slf4j(topic = "c.Test35")
public class test88888 {
    static AtomicReference<String>  ref = new AtomicReference<>("A");
    public static void main(String[] args) {
        log.debug("main.start...");
        final String pref = ref.get();
        other();
        Sleeper.sleep(1);
        log.debug("开始了第三次的切换:{}",ref.compareAndSet(pref, "c"));
    }
    private static void other() {
        new Thread(() -> log.debug("开始了第一次的切换:{}",ref.compareAndSet(ref.get(), "B")),"t1").start();
        Sleeper.sleep(0.5);
        new Thread(()-> log.debug("开始了第二次的切换:{}",ref.compareAndSet(ref.get(), "A")),"t2").start();
    }
}
