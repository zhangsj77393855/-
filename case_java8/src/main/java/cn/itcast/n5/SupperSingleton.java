package cn.itcast.n5;


/**
 * @Author：zsj
 * @Date：2023/8/10
 */
public final class SupperSingleton {
    private SupperSingleton() {
    }
    private static volatile SupperSingleton INSTANCE = null;

    public static SupperSingleton getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        // todo 这里再次判断是为了防止多个线程同时在锁外面等待,在前序线程没有重新赋值钱都判断INSTANCE为null
        //  当锁释放的时候 线程执行同步方法 但是现在INSTANCE已经赋值了 防止再次创建对象
        //  同时,对INSTANCE加上volatile关键字也防止了指令的重新排序  INSTANCE = new SupperSingleton();  防止先赋值再创建
        synchronized (SupperSingleton.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new SupperSingleton();
            return INSTANCE;
        }
    }
}
