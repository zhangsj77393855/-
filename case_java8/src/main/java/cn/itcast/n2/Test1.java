package cn.itcast.n2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> n1.a()).start();
        new Thread(() -> n1.b()).start();
    }
}
@Slf4j
class Number {
    public synchronized void a() {
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }
}
