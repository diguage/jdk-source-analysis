package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 19:16
 */
public class LockSupportTest {
    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(new Task("t1"));
        Thread t2 = new Thread(new Task("t2"));
        t1.start();
        Thread.sleep(1000);
        t2.start();
        LockSupport.unpark(t1);
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
        System.out.println("finish...");
    }

    static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            synchronized (LockSupportTest.class) {
                System.out.println("in " + name);
                LockSupport.park();
            }
        }
    }

    @Test
    public void testParkAndUnpark() throws InterruptedException {
        System.out.println("--m1------");
        Thread thread = new Thread(() -> {
            System.out.println("--t1------");
            LockSupport.park();
            System.out.println("--t2------");
        });
        thread.start();
        Thread.sleep(5000);
        LockSupport.unpark(thread);
        System.out.println("--m2------");
    }

}
