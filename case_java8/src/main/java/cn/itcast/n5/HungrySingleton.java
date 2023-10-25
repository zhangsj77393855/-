package cn.itcast.n5;

import java.io.Serializable;

/**
 * @Author：zsj
 * 饿汉式单例模式
 * @Date：2023/8/10
 */
public final class HungrySingleton implements Serializable {
    private HungrySingleton() {
    }

    private static final HungrySingleton INSTANCE = new HungrySingleton();

    public static HungrySingleton getINSTANCE() {
        return INSTANCE;
    }

    public Object resolve(){
        return INSTANCE;
    }
}
