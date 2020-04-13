package com.diguage.truman.concurrent;

import org.junit.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-31 11:41
 */
public class AbstractQueuedSynchronizerTest {

    @Test
    public void testNode() {
        AqsNode head = new AqsNode();
        AqsNode next = new AqsNode(AqsNode.EXCLUSIVE);
        head.next = next;
        next.prev = head;
        AqsNode tail = new AqsNode(AqsNode.EXCLUSIVE);
        next.next = tail;
        tail.prev = next;
        List<Thread> threads = new ArrayList<>();
        for (AqsNode node = head; node != null; node = node.next) {
            threads.add(node.thread);
        }
        System.out.println(threads);
    }

    public static class AqsNode {

        static final AqsNode SHARED = new AqsNode();
        static final AqsNode EXCLUSIVE = null;

        static final int CANCELLED = 1;
        static final int SIGNAL = -1;
        static final int CONDITION = -2;
        static final int PROPAGATE = -3;

        volatile int waitStatus;

        volatile AqsNode prev;

        volatile AqsNode next;

        volatile Thread thread;

        AqsNode nextWaiter;

        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        final AqsNode predecessor() {
            AqsNode p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        AqsNode() {
        }

        AqsNode(AqsNode nextWaiter) {
            this.nextWaiter = nextWaiter;
            THREAD.set(this, Thread.currentThread());
        }

        AqsNode(int waitStatus) {
            WAITSTATUS.set(this, waitStatus);
            THREAD.set(this, Thread.currentThread());
        }

        final boolean compareAndSetWaitStatus(int expect, int update) {
            return WAITSTATUS.compareAndSet(this, expect, update);
        }

        final boolean compareAndSetNext(AqsNode expect, AqsNode update) {
            return NEXT.compareAndSet(this, expect, update);
        }

        final void setPrevRelaxed(AqsNode p) {
            PREV.set(this, p);
        }

        private static final VarHandle NEXT;
        private static final VarHandle PREV;
        private static final VarHandle THREAD;
        private static final VarHandle WAITSTATUS;

        static {
            try {
                MethodHandles.Lookup l = MethodHandles.lookup();
                NEXT = l.findVarHandle(AqsNode.class, "next", AqsNode.class);
                PREV = l.findVarHandle(AqsNode.class, "prev", AqsNode.class);
                THREAD = l.findVarHandle(AqsNode.class, "thread", Thread.class);
                WAITSTATUS = l.findVarHandle(AqsNode.class, "waitStatus", int.class);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    @Test
    public void testCustomLock() throws InterruptedException {
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
