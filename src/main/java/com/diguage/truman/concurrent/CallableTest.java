package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-30 11:24
 */
public class CallableTest {
    @Test
    public void test() {
        Callable<Void> task = () -> {
            return null;
        };

    }

}
