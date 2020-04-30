package com.diguage.truman.concurrent;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-12 18:07
 */
public class ThreadTest {
    @Test
    public void testState() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("StartTime: " + LocalDateTime.now());
            int i = 0;
            try {
                Thread.sleep(10 * 1000);
                while (true) {
                    i++;
                    if (i > Integer.MAX_VALUE >> 1) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("testState: is interrupted at "
                        + LocalDateTime.now());
                e.printStackTrace();
            }
            System.out.println("  EndTime: " + LocalDateTime.now());
        });
        // NEW
        System.out.println(thread.getState());
        thread.start();
        // RUNNABLE
        System.out.println(thread.getState());
        Thread.sleep(1000);
        // TIMED_WAITING
        System.out.println(thread.getState());
        Thread.sleep(9200);
        // RUNNABLE ??
        System.out.println(thread.getState());
        Thread.sleep(10 * 1000);
        // TERMINATED
        System.out.println(thread.getState());
    }

    @Test
    public void testBlockState() throws InterruptedException {
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(50);
        System.out.println(t2.getState());
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
    public void testWaitLock() throws InterruptedException {
        // 测试 wait 是否释放锁
        // 根据运行结果来看，thread1 和 thread2 是交叉执行的，
        // 则：线程在 wait 时，是释放了锁的，
        // 再次获取锁后，会接着上次执行点继续执行。
        //
        // 这里还有一点需要注意：wait 需要在锁对象上执行，否则会报错。
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    lock.wait(1000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(2000);
    }

    @Test
    public void testSleepLock() throws InterruptedException {
        // 测试 sleep 是否释放锁
        // 根据输出来看，thread1 执行完后再次执行的 thread2
        // 则：线程在 sleep 时，不释放锁。
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    Thread.sleep(2000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(3000);
    }


    @Test
    public void testJoin() throws InterruptedException {
        JoinMain.AddThread thread = new JoinMain.AddThread();
        thread.start();
        // 执行这句话，则下面的输出会等 thread 执行完成后，i值等于100000；
        // 如果注释掉，则瞬间向下执行，i值很小。
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
    public void testYield() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
            }
        });
        thread.start();
        Thread.sleep(100);
        System.out.println(thread.getState());
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

    @Test
    public void testInterruptNoAction() {
        // 虽然给线程发出了中断信号，但程序中并没有响应中断信号的逻辑，所以程序不会有任何反应。
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.park();
    }

    @Test
    public void testInterruptAction() {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
    }

    @Test
    public void testInterruptFailure() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }

                try {
                    // sleep() 方法被中断后会清除中断标记，所以循环会继续运行。。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java技术栈线程休眠被中断，程序退出。");
                }
                System.out.println(Thread.currentThread().getState() + " 线程苏醒，继续执行……");
            }
        });
        thread.start();
        Thread.sleep(100); // 注意加上这句话！否则线程还没启动就被终端了
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testInterruptSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }

                try {
                    // sleep() 方法被中断后会清除中断标记，所以循环会继续运行。。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java技术栈 线程 休眠被中断，程序退出。");
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getState() + " 线程苏醒，继续执行……");
            }
        });
        thread.start();
        Thread.sleep(100); // 注意加上这句话！否则线程还没启动就被终端了
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testSynchronized() throws InterruptedException {
        class Account {
            int money = 100;

            synchronized void increase() {
                System.out.println("start to increase");
                money -= 10;
                double var = 0;
                for (int i = 0; i < 10000000; i++) {
                    var = Math.PI * Math.E * i;
                    if (i % 2000000 == 0) {
                        throw new RuntimeException("fire");
                    }
                }
                System.out.println("finish increasing." + var);
            }

            synchronized void decrease() {
                System.out.println("start to decrease");
                money += 20;
                System.out.println("finish decreasing.");
            }
        }
        Account account = new Account();
        new Thread(account::increase).start();
        Thread.sleep(1);
        new Thread(account::decrease).start();
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(30));
        System.out.println(account.money);
    }
}
