package com.diguage.truman.concurrent;

import org.junit.Test;
import org.junit.experimental.theories.Theories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-12 18:07
 */
public class ThreadTest {
    @Test
    public void testState() {
        Thread thread = new Thread(() -> {
            System.out.println("StartTime: " + LocalDateTime.now());
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                System.out.println("testState: is interrupted at "
                        + LocalDateTime.now());
                e.printStackTrace();
            }
            System.out.println("  EndTime: " + LocalDateTime.now());
        });
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        class InterruptTask implements Runnable {
            @Override
            public void run() {
                Thread.interrupted();
                Thread thread = Thread.currentThread();
                while (true) {
                    if (thread.isInterrupted()) {
                        System.out.println("InterruptTask was interrupted at "
                                + LocalDateTime.now());
                    }
//                    try {
//                        Thread.sleep(5 * 1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }

        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(20 * 1000);
        thread.interrupt();
    }

    @Test
    public void testWait() {

    }

    @Test
    public void testSleep() {

    }

    @Test
    public void testJoin() throws InterruptedException {
        JoinMain.AddThread thread = new JoinMain.AddThread();
        thread.start();
        // 执行这句话，则下面的输出会等 thread 执行完成后，i值等于100000；如果注释掉，则瞬间向下执行，i值很小。
        thread.join();
        System.out.println(JoinMain.i);
    }

    static class JoinMain {
        public volatile static int i = 0;

        static class AddThread extends Thread {
            @Override
            public void run() {
                for (i = 0; i < 100000; i++) {
                }
            }
        }
    }

    @Test
    public void testYield() {
        Thread.yield();
    }

    @Test
    public void testChildThread() {
        // TODO 如何掩饰父子线程？如何在父子线程之间传递数据？
        List<Thread> threads = new ArrayList<>();
        Thread thread1 = new Thread(() -> {
            Thread thread = Thread.currentThread();
            ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 119);

            Thread child = new Thread(() -> {
            });

            System.out.printf("id=%d, parentId=%d %n", thread.getId(), 123);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads.add(thread1);
        thread1.start();
    }

}
