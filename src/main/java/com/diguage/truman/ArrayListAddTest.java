package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2019-12-12 18:48
 */
public class ArrayListAddTest extends ArrayListBaseTest {
    // tag::testAddAtTail[]
    @Test
    public void testAddAtTail() {
        int initialCapacity = 8;
        ArrayList<Integer> integers = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity * 2; i++) {
            xray(integers);
            integers.add(i);
        }
    }
    // end::testAddAtTail[]

    // tag::testAddAtHeader[]
    @Test
    public void testAddAtHeader() {
        int initialCapacity = 8;
        ArrayList<Integer> integers = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity * 2; i++) {
            xray(integers);
            integers.add(0, i);
        }
    }
    // end::testAddAtHeader[]
}
