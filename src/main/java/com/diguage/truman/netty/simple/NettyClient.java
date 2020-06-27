package com.diguage.truman.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 19:35
 */
public class NettyClient {
  @Test
  public void client() throws InterruptedException {
    // 客户端只需要一个事件循环组即可
    NioEventLoopGroup group = new NioEventLoopGroup();

    try {
      // 创建客户端启动对象
      Bootstrap bootstrap = new Bootstrap();
      // 设置相关参数
      bootstrap.group(group) // 设置线程组
        .channel(NioSocketChannel.class) // 设置客户端通讯通道的实现类
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyClientHandler()); // 加入自己的处理器
          }
        });
      System.out.println("....客户端 OK ...");

      // 启动客户端去连接服务器端
      // 关于 ChannelFuture 还要分析，涉及到 Netty 的异步模型
      ChannelFuture future = bootstrap.connect("127.0.0.1", 11911).sync();

      // 给关闭通道进行监听
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }
}
