package com.diguage.truman.netty.dubbo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 17:32
 */
public class NettyClient {
  // 创建线程池
  private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static NettyClientHandler client;

  // 使用 代理模式，创建代理对象
  public Object getBean(final Class<?> serviceService, final String providerName) {
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
      new Class<?>[]{serviceService}, (proxy, method, args) -> {
        if (client == null) {
          initClient();
        }
        // 设置要发送给服务器的消息
        // providerName 协议头，args[0] 就是客户端调用 API hello(msg) 时，传的参数
        client.setParam(providerName + args[0]);
        return executor.submit(client).get();
      });
  }

  private static void initClient() {
    client = new NettyClientHandler();
    NioEventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group)
      .channel(NioSocketChannel.class)
      .option(ChannelOption.TCP_NODELAY, true)
      .handler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
          ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast(new StringDecoder());
          pipeline.addLast(new StringEncoder());
          pipeline.addLast(client);
        }
      });
    try {
      bootstrap.connect("127.0.0.1", 11911).sync();
      System.out.println("客户端建立链接……");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
