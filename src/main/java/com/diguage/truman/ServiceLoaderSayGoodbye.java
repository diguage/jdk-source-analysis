package com.diguage.truman;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 10:48
 */
public class ServiceLoaderSayGoodbye implements ServiceLoaderSay {
    @Override
    public void say() {
        System.out.println("Goodbye, https://www.diguage.com/");
    }
}
