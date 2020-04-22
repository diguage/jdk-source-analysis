package com.diguage.truman.concurrent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 20:39
 */
public class FilesSpy {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        String homePath = System.getProperty("user.home");
        TreeNode root = new TreeNode(homePath);
        pool.execute(new FileSizeTask(root));
    }

    public static class FileSizeTask extends RecursiveTask<Integer> {
        private final TreeNode root;

        public FileSizeTask(TreeNode root) {
            this.root = root;
        }

        @Override
        protected Integer compute() {
            return null;
        }
    }
    public static class TreeNode {
        private final TreeNode parent;
        private final File file;
        private final String absolutePath;
        private final String name;
        private AtomicLong size = new AtomicLong(0);
        private Comparator<TreeNode> comparator = Comparator.comparingLong(TreeNode::length);
        private TreeMap<TreeNode, TreeNode> pathToNode;
        private ReentrantReadWriteLock lock;
        private ReentrantReadWriteLock.WriteLock writeLock;
        private ReentrantReadWriteLock.ReadLock readLock;

        public TreeNode(String absolutePath) {
            this(absolutePath, null);
        }

        public TreeNode(String absolutePath, TreeNode parent) {
            this(new File(absolutePath), parent);
        }

        public TreeNode(File file, TreeNode parent) {
            this.parent = parent;
            this.absolutePath = file.getAbsolutePath();
            this.file = file;
            this.name = this.file.getName();
            if (this.file.isFile()) {
                long bytes = file.length();
                long len = 1;
                if (bytes > 1024) {
                    len = (bytes & (1024 - 1)) == 0 ? bytes / 1024 : bytes / 1024 + 1;
                    size = new AtomicLong(len);
                }
            } else {
                this.lock = new ReentrantReadWriteLock();
                this.writeLock = lock.writeLock();
                this.readLock = lock.readLock();
                pathToNode = new TreeMap<>(comparator);
            }
        }

        public List<File> getSubfiles() {
            try {
                readLock.lock();
                if (Objects.isNull(pathToNode) || pathToNode.isEmpty()) {
                    return Collections.emptyList();
                }
                NavigableSet<TreeNode> treeNodes = pathToNode.navigableKeySet();
                List<File> result = new ArrayList<>(treeNodes.size());
                for (TreeNode node : treeNodes) {
                    result.add(node.getFile());
                }
                return result;
            } finally {
                readLock.unlock();
            }
        }

        public void add(File file) {
            if (Objects.isNull(file) || !file.exists()) {
                throw new IllegalArgumentException("文件必须存在！");
            }
            if (isFile()) {
                throw new UnsupportedOperationException("文件没有子目录！");
            }
            if (!file.getAbsolutePath().startsWith(getAbsolutePath())) {
                throw new IllegalArgumentException("只能添加子目录！");
            }
            try {
                writeLock.lock();
                TreeNode node = new TreeNode(file, this);
                if (contains(node)) {
                    return;
                }
                pathToNode.put(node, node);
                // 更新大小
                TreeNode temp = this;
                while (Objects.nonNull(temp)) {
                    temp.getSize().getAndAdd(node.length());
                    temp = temp.parent;
                }
            } finally {
                writeLock.unlock();
            }
        }


        public File getFile() {
            return file;
        }

        public boolean isFile() {
            return file.isFile();
        }

        public boolean isPath() {
            return !isFile();
        }

        public String getAbsolutePath() {
            return absolutePath;
        }

        public long length() {
            return size.get();
        }

        public AtomicLong getSize() {
            return this.size;
        }

        public boolean contains(TreeNode treeNode) {
            try {
                readLock.lock();
                return pathToNode.containsKey(treeNode);
            } finally {
                readLock.unlock();
            }
        }

        public String getName() {
            return name;
        }

        public TreeNode getParent() {
            return parent;
        }
    }
}
