package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 17:23
 */
public class CountDownLatchTest {
    private int count = 2;

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(new Task(latch));
        }
        latch.await();
        System.out.println("Fire...");
        executorService.shutdown();
        while (executorService.isTerminated()) {
        }
        System.out.println("All task were done.");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
        System.out.println("Terminal at " + LocalDateTime.now());
    }

    static class Task implements Runnable {
        private final CountDownLatch latch;

        public Task(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                int time = new Random().nextInt(5000);
                latch.countDown();
                System.out.println(Thread.currentThread().getId() + " time = " + LocalDateTime.now());
                Thread.sleep(time);
                System.out.println(Thread.currentThread().getId() + " sleep = " + time + ": check finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
