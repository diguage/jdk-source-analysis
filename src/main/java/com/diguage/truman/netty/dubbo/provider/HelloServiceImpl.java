package com.diguage.truman.netty.dubbo.provider;

import com.diguage.truman.netty.dubbo.interfaces.HelloService;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 16:55
 */
public class HelloServiceImpl implements HelloService {

  private static int count = 0;

  @Override
  public String hello(String msg) {
    System.out.println("接收到客户端消息=" + msg);
    if (msg != null) {
      return "您好，D瓜哥！我接收到了你的消息[" + msg + "] 第" + (++this.count) + "次";
    } else {
      return "对不起！没有收到你的消息！";
    }
  }
}
