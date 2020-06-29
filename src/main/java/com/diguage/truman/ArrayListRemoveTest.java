package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-03 11:35
 */
public class ArrayListRemoveTest extends ArrayListBaseTest {
    @Test
    public void testRemoveTail() {
        int initialCapacity = 8;
        ArrayList<Integer> integers = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity * 2; i++) {
            xray(integers);
            integers.add(i);
        }

        for (int i = initialCapacity * 2 - 1; i >= 0; i--) {
            xray(integers);
            integers.remove(i);
        }
    }

    @Test
    public void testRemoveHeader() {
        int initialCapacity = 8;
        ArrayList<Integer> integers = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity * 2; i++) {
            integers.add(i);
        }

        for (int i = 0; i < initialCapacity * 2; i++) {
            xray(integers);
            integers.remove(i);
        }
    }
}
