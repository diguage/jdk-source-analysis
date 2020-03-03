package com.diguage.truman;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-03 11:10
 */
public class ArrayListBaseTest {
    /**
     * 通过反射查看 {@link ArrayList} 的内部属性
     */
    public void xray(ArrayList list) {
        Class clazz = list.getClass();
        try {
            Field elementData = clazz.getDeclaredField("elementData");
            elementData.setAccessible(true);
            Object[] objects = (Object[]) elementData.get(list);
            Field sizeField = clazz.getDeclaredField("size");
            sizeField.setAccessible(true);

            int size = 0;
            for (int i = 0; i < objects.length; i++) {
                if (Objects.nonNull(objects[i])) {
                    ++size;
                }
            }
            System.out.println("length = " + objects.length
                    + ", size = " + sizeField.get(list)
                    + ", arraySize = " + size);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
