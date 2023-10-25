package cn.itcast.n5;

/**
 * @Author：zsj
 * 效率低下的单例模式 懒汉式
 * @Date：2023/8/10
 */
public final class ShowSingletom {
    private ShowSingletom() {
    }

    private static ShowSingletom INSTANCE = null;

    // todo 这种编码可以实现代码的同步性 但是锁的范围太大了 性能低下
    //  因为这个仅仅需要第一次加载的时候获取锁  但是目前是后续的每次调用都需要进行加锁
    //  其次,同一时间只有一个线程能过获取到锁,其它的线程需要等待 效率低下
    //  最后,有可能会涉及到指令的重排序,sync只能保证完全被sync操作的变量的有序性,并不能保证同步代码块中的重排序
    //  而且,sync里面的重排序对外部并不可见  比如有可能先赋值 new ShowSingletom();
    public static synchronized ShowSingletom getInstance(){
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new ShowSingletom();
        return INSTANCE;
    }
}
