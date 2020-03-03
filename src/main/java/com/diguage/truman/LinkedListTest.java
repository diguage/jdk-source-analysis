package com.diguage.truman;

import org.junit.Test;

import java.util.LinkedList;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-03 15:53
 */
public class LinkedListTest extends LinkedListBaseTest {

    // tag::testAddAtTail[]
    @Test
    public void testAddAtTail() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            xray(list);
            list.add(i);
        }
    }
    // end::testAddAtTail[]

    // tag::testAddAtHeader[]
    @Test
    public void testAddAtHeader() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            xray(list);
            list.addFirst(i);
        }
    }
    // end::testAddAtHeader[]
}
