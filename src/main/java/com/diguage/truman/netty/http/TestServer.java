package com.diguage.truman.netty.http;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 23:33
 */
public class TestServer {

  @Test
  public void test() throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new TestServerInitializer());

      ChannelFuture future = bootstrap.bind(11911).sync();

      future.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
