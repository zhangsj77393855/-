package cn.itcast.n7.threadLocal;

/**
 * @Author：zsj
 * @Date：2023/8/11
 */
public class MyDemo {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private String content;

    public String getContent() {
        return threadLocal.get();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void main(String[] args) {
        final MyDemo myDemo = new MyDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                myDemo.setContent(Thread.currentThread().getName() + "的数据");
                System.out.println("----------------------------");
                System.out.println(Thread.currentThread().getName() + "/////" + myDemo.getContent());
            },"线程" + i).start();
        }
    }
}
