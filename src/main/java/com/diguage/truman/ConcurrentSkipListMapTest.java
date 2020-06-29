package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-08 16:43
 */
public class ConcurrentSkipListMapTest {
    @Test
    public void test() {
        ConcurrentSkipListMap<Integer, Integer> map = new ConcurrentSkipListMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }
    }
}
