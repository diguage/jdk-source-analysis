package com.diguage.truman;

import org.junit.Test;

import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 20:48
 */
public class PriorityQueueTest {
    // tag::size[]
    @Test
    public void testSize() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(5);
        for (int i = 0; i < 10; i++) {
            queue.add(i);
        }
        assertThat(queue).hasSize(10);
    }

    @Test
    public void test() {
        int capacity = 5;
        PriorityQueue<Integer> queue = new PriorityQueue<>(capacity);
        for (int i = 0; i < 10; i++) {
            queue.add(i);
            if (queue.size() > capacity) {
                Integer num = queue.poll();
                System.out.println(num);
            }
        }
        assertThat(queue).hasSize(capacity);
        assertThat(queue).contains(9);
        assertThat(queue).doesNotContain(0);
        Integer num = queue.poll();
        // 可见，默认就是最小堆。
        assertThat(num).isEqualTo(5);
    }
    // end::size[]
}
