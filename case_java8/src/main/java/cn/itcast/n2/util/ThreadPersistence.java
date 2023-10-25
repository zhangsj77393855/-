package cn.itcast.n2.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 定义数据模型
@AllArgsConstructor
@Data
@NoArgsConstructor
class ThreadData {
    private int threadId;
    private String status;
    private String data;
}

// 模拟执行任务的线程
class TaskThread extends Thread {
    private int threadId;
    private String status;
    private String data;

    public TaskThread(int threadId) {
        this.threadId = threadId;
        this.status = "Running";
        this.data = "Thread data";
    }

    public void run() {
        // 模拟任务执行
        while (status.equals("Running")) {
            System.out.println("Thread " + threadId + " is running... Data: " + data);
        }
    }

    // 中止线程
    public void stopThread() {
        this.status = "Stopped";
    }
}

// 持久化数据到数据库
public class ThreadPersistence {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/thread_data";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void saveThreadData(ThreadData threadData) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO threads (thread_id, status, data) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, threadData.getThreadId());
            stmt.setString(2, threadData.getStatus());
            stmt.setString(3, threadData.getData());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 从数据库恢复线程数据
    public static ThreadData restoreThreadData(int threadId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM threads WHERE thread_id = " + threadId;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int restoredThreadId = rs.getInt("thread_id");
                String restoredStatus = rs.getString("status");
                String restoredData = rs.getString("data");
                return new ThreadData(restoredThreadId, restoredStatus, restoredData);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // 创建一个线程并启动
        TaskThread thread = new TaskThread(1);
        thread.start();

        // 模拟项目重启后恢复线程数据
        ThreadData restoredData = restoreThreadData(1);
        if (restoredData != null) {
            thread = new TaskThread(restoredData.getThreadId());
            thread.start();
        } else {
            // 如果没有持久化数据，则继续执行新的任务
            ThreadData newThreadData = new ThreadData(2, "Running", "New Thread Data");
            thread = new TaskThread(newThreadData.getThreadId());
            thread.start();
            saveThreadData(newThreadData);
        }
    }
}
