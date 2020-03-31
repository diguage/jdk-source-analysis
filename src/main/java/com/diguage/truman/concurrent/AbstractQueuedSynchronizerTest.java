package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-31 11:41
 */
public class AbstractQueuedSynchronizerTest {
    @Test
    public void test() throws InterruptedException {
        Mutex mutex = new Mutex();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    int time = new Random().nextInt(5000);
                    mutex.lock();
                    System.out.printf("thread=%d running time=%d%n",
                            Thread.currentThread().getId(), time);
                    Thread.sleep(time);
                    System.out.printf("thread=%d finished%n",
                            Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.unlock();

                }
            });
        }
        executorService.shutdown();
        while (executorService.isTerminated()) {
        }
        Thread.sleep(50000);
        System.out.println("All task were finished.");
    }
}
