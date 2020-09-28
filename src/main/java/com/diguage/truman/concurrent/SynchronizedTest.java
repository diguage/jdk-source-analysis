package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.LockSupport;

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

  @Test
  public void testInstanceLock() {
    SynMain main = new SynMain();
    new Thread(main::getInstanceLock1).start();
    new Thread(main::getInstanceLock2).start();
    new Thread(SynMain::getStaticLock1).start();
    new Thread(SynMain::getStaticLock2).start();
    LockSupport.park();
  }


  public static class SynMain {
    public static synchronized void getStaticLock1() {
      System.out.println("getStaticLock1 get lock, running...");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public static synchronized void getStaticLock2() {
      System.out.println("getStaticLock2 get lock, running...");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public synchronized void getInstanceLock1() {
      System.out.println("getInstanceLock1 get lock, running...");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public synchronized void getInstanceLock2() {
      System.out.println("getInstanceLock2 get lock, running...");
      try {
        Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
