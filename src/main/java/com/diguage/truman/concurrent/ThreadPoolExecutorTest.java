package com.diguage.truman.concurrent;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-10 10:50
 */
public class ThreadPoolExecutorTest {

    @Test
    public void testStatus() {
        int COUNT_BITS = Integer.SIZE - 3;
        int COUNT_MASK = (1 << COUNT_BITS) - 1;

        // runState is stored in the high-order bits
        int RUNNING = -1 << COUNT_BITS;
        int SHUTDOWN = 0 << COUNT_BITS;
        int STOP = 1 << COUNT_BITS;
        int TIDYING = 2 << COUNT_BITS;
        int TERMINATED = 3 << COUNT_BITS;

        System.out.printf("%32s // %d%n", Integer.toBinaryString(RUNNING), RUNNING);
        System.out.printf("%32s // %d%n", Integer.toBinaryString(SHUTDOWN), SHUTDOWN);
        System.out.printf("%32s // %d%n", Integer.toBinaryString(STOP), STOP);
        System.out.printf("%32s // %d%n", Integer.toBinaryString(TIDYING), TIDYING);
        System.out.printf("%32s // %d%n", Integer.toBinaryString(TERMINATED), TERMINATED);
    }

    @Test
    public void testPoolSize() {
        ThreadPoolExecutor executorService
                = new ThreadPoolExecutor(2, 4, 1L,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(6));

        for (int i = 0; i < 10; i++) {
            executorService.execute(new Task("Task-" + i));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finish all thread...");
    }

    @Test
    public void testCallable() {
        ThreadPoolExecutor executorService
                = new ThreadPoolExecutor(2, 4, 1L,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(6));

        List<Future<String>> futures = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(
                    new CallableTask("CallableTask-" + i));

            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                System.out.println(LocalDateTime.now() + "::" + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finish all thread...");
    }

    @Test
    public void testComplete() {
        ThreadPoolExecutor executorService
                = new ThreadPoolExecutor(2, 4, 1L,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(6));

        List<Future<String>> futures = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(
                    new CallableTask("CallableTask-" + i));
            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                System.out.println(LocalDateTime.now() + "::" + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finish all thread...");
    }

    public static class CallableTask implements Callable<String> {
        private final String name;

        public CallableTask(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            //返回执行当前 Callable 的线程名字
            return Thread.currentThread().getName();
        }
    }

    public static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()
                    + " Start. Time = " + LocalDateTime.now());

            processCommand();

            System.out.println(Thread.currentThread().getName()
                    + " End. Time = " + LocalDateTime.now());
        }

        private void processCommand() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
