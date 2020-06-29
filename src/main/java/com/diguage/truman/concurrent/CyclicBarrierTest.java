package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 17:24
 */
public class CyclicBarrierTest {
    @Test
    public void test() {
        CyclicBarrier barrier = new CyclicBarrier(5,
                () -> System.out.println("集合完毕，出发……"));
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Task(barrier, "Task-" + (i + 1)));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finished.");
    }

    static class Task implements Runnable {
        private final CyclicBarrier barrier;
        private final String name;

        public Task(CyclicBarrier barrier, String name) {
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + " start...");
                barrier.await();
                Thread.sleep(new Random().nextInt(1000));
                System.out.println(name + " running...");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
