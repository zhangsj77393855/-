package cn.itcast.n8.mytestthreadpool;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskDistributionExample {
    public static void main(String[] args) {
        int totalTasks = 99;
        int threads = 5;

        // 计算每个线程分配的任务数量
        int tasksPerThread = totalTasks / threads;  // 每个线程的任务数量

        List<Task> taskList = createTaskList(totalTasks);

        // 使用Lists.partition()方法将任务列表分割成多个子列表
        List<List<Task>> subTaskLists = Lists.partition(taskList, tasksPerThread);

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        // 提交子任务给线程池执行，并收集Future结果
        List<Future<List<String>>> futures = new ArrayList<>();
        for (List<Task> subTaskList : subTaskLists) {
            Future<List<String>> future = executorService.submit(new SubTask(subTaskList));
            futures.add(future);
        }

        // 收集每个任务执行的结果
        List<String> resultList = new ArrayList<>();
        for (Future<List<String>> future : futures) {
            try {
                resultList.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 输出结果
        for (String result : resultList) {
            System.out.println(result);
        }

        // 关闭线程池
        executorService.shutdown();
    }

    // 创建任务列表
    private static List<Task> createTaskList(int totalTasks) {
        List<Task> taskList = new ArrayList<>();
        for (int i = 1; i <= totalTasks; i++) {
            taskList.add(new Task(i));
        }
        return taskList;
    }

    static class Task {
        private final int id;

        Task(int id) {
            this.id = id;
        }

        public String execute() {
            // 执行任务操作，并返回结果
            return "Task " + id + " executed by " + Thread.currentThread().getName();
        }
    }

    static class SubTask implements Callable<List<String>> {
        private final List<Task> taskList;

        SubTask(List<Task> taskList) {
            this.taskList = taskList;
        }

        @Override
        public List<String> call() {
            List<String> resultList = new ArrayList<>();
            for (Task task : taskList) {
                String result = task.execute();
                resultList.add(result);
            }
            return resultList;
        }
    }
}
