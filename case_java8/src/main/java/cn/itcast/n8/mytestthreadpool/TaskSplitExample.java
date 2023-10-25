package cn.itcast.n8.mytestthreadpool;

import java.util.concurrent.*;

public class TaskSplitExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int totalTasks = 10;
        int tasksPerThread = 2;
        int threads = totalTasks / tasksPerThread;

        CyclicBarrier barrier = new CyclicBarrier(threads + 1);

        for (int i = 0; i < threads; i++) {
            int start = i * tasksPerThread + 1;
            int end = start + tasksPerThread - 1;
            executor.execute(new SubTask(start, end, barrier));
        }

        try {
            barrier.await(); // 等待所有子任务执行完成
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");

        executor.shutdown();
    }

    static class SubTask implements Runnable {
        private final int start;
        private final int end;
        private final CyclicBarrier barrier;

        SubTask(int start, int end, CyclicBarrier barrier) {
            this.start = start;
            this.end = end;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                // 执行子任务
                System.out.println("Task " + i + " executed by " + Thread.currentThread().getName());
            }

            try {
                barrier.await(); // 子任务执行完成，通知主线程
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
