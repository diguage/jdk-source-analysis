package com.diguage.truman;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 10:48
 */
public class ServiceLoaderSayHello implements ServiceLoaderSay {
    @Override
    public void say() {
        System.out.println("Hello, https://www.diguage.com/");
    }
}
