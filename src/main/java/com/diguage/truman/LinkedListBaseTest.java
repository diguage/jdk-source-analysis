package com.diguage.truman;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-03 16:16
 */
public class LinkedListBaseTest {
    /**
     * 使用反射读取 LinkedList 内部属性
     */
    public void xray(LinkedList<?> list) {
        Class<? extends LinkedList> clazz = list.getClass();
        try {
            Field nodeField = clazz.getDeclaredField("first");
            nodeField.setAccessible(true);
            Object node = nodeField.get(list);
            System.out.println("length=" + length(node) + ", size=" + list.size());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int length(Object node) {
        int result = 0;
        if (Objects.isNull(node)) {
            return result;
        }
        try {
            Class<?> nodeClass = node.getClass();
            Field nextField = nodeClass.getDeclaredField("next");
            nextField.setAccessible(true);
            while (Objects.nonNull(node)) {
                node = nextField.get(node);
                result++;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
