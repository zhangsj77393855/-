package cn.itcast.n5;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class CollectResultsExample {
    public static void main(String[] args) {
        final StopWatch stopWatch = new StopWatch();
        // 开始计时
        stopWatch.start();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        int totalTasks = 99;
        int threads = 5;

        LinkedBlockingQueue<Integer> resultQueue = new LinkedBlockingQueue<>();

        List<Integer> taskList = new ArrayList<>();
        for (int i = 1; i <= totalTasks; i++) {
            taskList.add(i);
        }
        Collections.shuffle(taskList); // 打乱任务列表

        // 按顺序分配任务给线程
        int tasksPerThread = totalTasks / threads;
        for (int i = 0; i < threads; i++) {
            int start = i * tasksPerThread;
            int end = (i == threads - 1) ? totalTasks - 1 : start + tasksPerThread - 1;
            List<Integer> subTaskList = taskList.subList(start, end + 1);
            executor.execute(new SubTask(subTaskList, resultQueue));
        }

        // 收集结果
        for (int i = 0; i < totalTasks; i++) {
            try {
                int result = resultQueue.take();
                System.out.println("Task " + result + " executed.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    static class SubTask implements Runnable {
        private final List<Integer> taskList;
        private final LinkedBlockingQueue<Integer> resultQueue;

        SubTask(List<Integer> taskList, LinkedBlockingQueue<Integer> resultQueue) {
            this.taskList = taskList;
            this.resultQueue = resultQueue;
        }

        @Override
        public void run() {
            for (int task : taskList) {
                // 执行子任务，并将结果放入队列
                // 这里只是一个示例，可以根据实际任务编写具体的逻辑
                int result = executeTask(task);
                try {
                    resultQueue.put(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private int executeTask(int task) {
            // 在这里执行任务的逻辑，可以根据任务的需求编写具体的代码
            // 这里只是一个示例，生成一个随机数作为任务的执行结果
            return task; // 这里直接返回任务本身
        }
    }
}
