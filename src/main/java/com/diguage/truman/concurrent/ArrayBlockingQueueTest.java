package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 15:11
 */
public class ArrayBlockingQueueTest {

    @Test
    public void testTimeoutPoll() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ArrayBlockingQueue<Long> queue = new ArrayBlockingQueue<Long>(5);
        executorService.execute(() -> {
            for (long i = 0; i < 5; i++) {
                queue.add(i);
                try {
                    Thread.sleep(2 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for (int i = 0; i < 10; i++) {
            final long time = i;
            executorService.execute(() -> {
                try {
                    Long num = queue.poll(time, TimeUnit.MINUTES);
                    System.out.println("poll:" + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(10));
    }
}
