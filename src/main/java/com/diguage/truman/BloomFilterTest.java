package com.diguage.truman;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-10 14:38
 */
public class BloomFilterTest {
    @Test
    public void test() {
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 1500, 0.01);
        System.out.println(filter.mightContain(1));
        System.out.println(filter.mightContain(2));

        filter.put(1);
        filter.put(2);

        System.out.println(filter.mightContain(1));
        System.out.println(filter.mightContain(2));
    }
}
