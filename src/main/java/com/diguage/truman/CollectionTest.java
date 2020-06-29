package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.Collection;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2019-12-12 18:40
 */
public abstract class CollectionTest {
    public abstract Collection<Integer> newInstance();

    public abstract int initLength();

    public abstract int totalLength();

    @Test
    public void add() {
        Collection<Integer> integers = newInstance();
        int start = 1;
        for (int i = 0; i < initLength(); i++) {
            integers.add(start++);
        }

        for (int i = start - 1; i < totalLength(); i++) {
            integers.add(start++);
        }
    }

    @Test
    public void remove() {

    }
}
