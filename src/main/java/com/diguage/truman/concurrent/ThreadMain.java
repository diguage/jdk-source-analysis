package com.diguage.truman.concurrent;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-09 19:28
 */
public class ThreadMain {
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfos) {
            System.out.printf("[%d]%s\n", info.getThreadId(), info.getThreadName());
        }
    }
}
