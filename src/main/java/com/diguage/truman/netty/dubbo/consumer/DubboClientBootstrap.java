package com.diguage.truman.netty.dubbo.consumer;

import com.diguage.truman.netty.dubbo.interfaces.HelloService;
import com.diguage.truman.netty.dubbo.netty.NettyClient;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 17:44
 */
public class DubboClientBootstrap {
  // 这里定义协议头
  public static final String providerName = "#Hello#";

  public static void main(String[] args) throws InterruptedException {
    NettyClient consumer = new NettyClient();
    HelloService helloService = (HelloService) consumer.getBean(HelloService.class, providerName);
    // 通过代理对象调用服务提供者的方法
    for (int i = 0; i < 100; i++) {
      Thread.sleep(1000);
      String result = helloService.hello("您好啊，Dubbo~~~" + i);
      System.out.println("调用结果 res=" + result);
    }
  }
}
