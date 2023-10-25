package cn.itcast.n8.mytestthreadpool;

import java.util.concurrent.*;

public class CollectResultsExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int totalTasks = 10;
        int tasksPerThread = 2;
        int threads = totalTasks / tasksPerThread;

        LinkedBlockingQueue<String> resultQueue = new LinkedBlockingQueue<>();

        for (int i = 0; i < threads; i++) {
            int start = i * tasksPerThread + 1;
            int end = start + tasksPerThread - 1;
            executor.execute(new SubTask(start, end, resultQueue));
        }

        // 收集结果
        for (int i = 0; i < threads; i++) {
            try {
                String result = resultQueue.take();
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    static class SubTask implements Runnable {
        private final int start;
        private final int end;
        private final LinkedBlockingQueue<String> resultQueue;

        SubTask(int start, int end, LinkedBlockingQueue<String> resultQueue) {
            this.start = start;
            this.end = end;
            this.resultQueue = resultQueue;
        }

        @Override
        public void run() {
            String result = "";
            for (int i = start; i <= end; i++) {
                // 执行子任务，并将结果保存
                result += "Task " + i + " executed by " + Thread.currentThread().getName() + "\n";
            }

            try {
                Thread.sleep(2000);
                resultQueue.put(result); // 将结果放入队列
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
