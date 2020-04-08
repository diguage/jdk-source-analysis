package com.diguage.truman.concurrent;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 19:26
 */
public class SynchronizedTest {
    public synchronized void lockMethod() {
        System.out.println("lock method");
    }

    public void lockObject() {
        synchronized (this) {
            System.out.println("lock object");
        }
    }
}
