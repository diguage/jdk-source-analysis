package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 10:40
 */
public class ServiceLoaderTest {
    @Test
    public void test() {
        ServiceLoader<ServiceLoaderSay> loader
                = ServiceLoader.load(ServiceLoaderSay.class);
        Iterator<ServiceLoaderSay> iterator = loader.iterator();
        while (iterator.hasNext()) {
            ServiceLoaderSay say = iterator.next();
            say.say();
        }
    }
}
