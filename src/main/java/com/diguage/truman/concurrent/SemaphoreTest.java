package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 16:51
 */
public class SemaphoreTest {
    @Test
    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Task(semaphore));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Ok...");
    }

    static class Task implements Runnable {
        private final Semaphore semaphore;

        public Task(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getId() + " :done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

}
