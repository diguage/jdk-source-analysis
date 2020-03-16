package com.diguage.truman.concurrent;

import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-12 10:54
 */
public class ForkJoinPoolTest {
    @Test
    public void test() {
        ForkJoinPool pool = new ForkJoinPool(2);
        FileCountTask task = new FileCountTask("/Users/diguage/Documents/wiki.diguage.com/java/jdk-source-analysis");
        ForkJoinTask<Integer> result = pool.submit(task);
        try {
            Integer count = result.get();
            System.out.println("file count = " + count);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        System.out.println("All thread finish...");
    }

    public static class FileCountTask extends RecursiveTask<Integer> {
        private File file;

        public FileCountTask(File file) {
            this.file = file;
        }

        public FileCountTask(String file) {
            this.file = new File(file);
        }

        @Override
        protected Integer compute() {
            int count = 0;
            if (file.isFile()) {
                count += 1;
            } else {
                File[] files = file.listFiles();
                List<FileCountTask> subTasks = new LinkedList<>();
                for (File f : files) {
                    FileCountTask task = new FileCountTask(f);
                    subTasks.add(task);
                    task.fork();
                }
                for (FileCountTask subTask : subTasks) {
                    count += subTask.join();
                }
            }
            return count;
        }
    }

}
