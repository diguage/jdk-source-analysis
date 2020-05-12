package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

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

    @Test
    public void testReentrant() {
        // 将 Semaphore 的参数分别设置成 1 和 5 运行看结果
        // 递归调用的次数跟 Semaphore 的参数一致
        // 说明，如果 Semaphore 参数为 1 时，它不支持重入。
        Semaphore semaphore = new Semaphore(5);
        class Task implements Runnable {
            private final Semaphore semaphore;
            private int len = 1;

            public Task(Semaphore semaphore) {
                this.semaphore = semaphore;
            }

            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println(len++);
                    run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
        new Thread(new Task(semaphore)).start();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
    }
}
