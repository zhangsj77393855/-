package cn.itcast.n8.mytestthreadpool;

import java.util.concurrent.*;

public class TaskPartitionExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int totalTasks = 10;
        int tasksPerThread = 2;
        int threads = totalTasks / tasksPerThread;

        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < threads; i++) {
            int start = i * tasksPerThread + 1;
            int end = start + tasksPerThread - 1;
            completionService.submit(new SubTask(start, end));
        }

        for (int i = 0; i < threads; i++) {
            try {
                Future<String> future = completionService.take(); // 获取已完成的子任务结果
                String result = future.get(); // 获取子任务的执行结果
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    static class SubTask implements Callable<String> {
        private final int start;
        private final int end;

        SubTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String call() throws Exception {
            String result = "";
            for (int i = start; i <= end; i++) {
                // 执行子任务，并将结果保存
                result += "Task " + i + " executed by " + Thread.currentThread().getName() + "\n";
            }

            return result;
        }
    }
}
