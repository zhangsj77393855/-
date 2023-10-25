package cn.itcast.n7.threadLocal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：zsj
 * @Date：2023/8/11
 */
public class ThreadLocalDateUtil {
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        }
    };

    // 等同于
    private static ThreadLocal<DateFormat> threadLocal1= ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"));


    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }
    public static String format(Date date) {
        return threadLocal.get().format(date);
    }
}
