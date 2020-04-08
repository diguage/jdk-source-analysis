package com.diguage.truman.concurrent;

import org.junit.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-09 20:20
 */
public class ThreadLocalTest {
    @Test
    public void test() {
        new Thread(new FirstApp()).start();
    }

    @Test
    public void testInheritableThreadLocal() {
        // TODO: InheritableThreadLocal
    }

    private static class FirstApp implements Runnable {
        private ThreadLocal<String> threadLocal
                = ThreadLocal.withInitial(() -> "FirstApp-1");

        private ThreadLocal<String> threadLocal2
                = ThreadLocal.withInitial(() -> "FirstApp-2");

        private SecondApp secondApp = new SecondApp();
        private ThridApp thridApp = new ThridApp();

        @Override
        public void run() {
            System.out.println(threadLocal.get());
            System.out.println(threadLocal2.get());
            new Thread(secondApp).start();
            thridApp.run();
        }
    }

    private static class SecondApp implements Runnable {
        private ThreadLocal<String> threadLocal
                = ThreadLocal.withInitial(() -> "SecondApp");

        @Override
        public void run() {
            System.out.println(threadLocal.get());
        }
    }

    private static class ThridApp implements Runnable {
        private ThreadLocal<String> threadLocal
                = ThreadLocal.withInitial(() -> getClass().getName());

        @Override
        public void run() {
            threadLocal.set("new-ThridApp-value");
            System.out.println(threadLocal.get());
        }
    }
}


