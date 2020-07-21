package com.diguage.truman.netty.dubbo.provider;

import com.diguage.truman.netty.dubbo.netty.NettyServer;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 16:58
 */
public class DubboServerBootstrap {
  public static void main(String[] args) {
    NettyServer.startServer("127.0.0.1", 11911);
  }
}
