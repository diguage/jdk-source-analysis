package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 17:23
 */
public class CountDownLatchTest {
    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Task(latch));
        }
        latch.await();
        System.out.println("Fire...");
        executorService.shutdown();
    }

    static class Task implements Runnable {
        private final CountDownLatch latch;

        public Task(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(5000));
                System.out.println(Thread.currentThread().getId() + " : check finished.");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
