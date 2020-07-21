package com.diguage.truman.netty.chats;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 10:56
 */
public class GroupChatServer {
  private int port;

  public GroupChatServer(int port) {
    this.port = port;
  }

  // 处理客户端请求
  public void run() throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            // 获取 pipeline
            ChannelPipeline pipeline = ch.pipeline();
            // 向 pipeline 加入解码器
            pipeline.addLast("decoder", new StringDecoder());
            // 向 pipeline 加入编码器
            pipeline.addLast("encoder", new StringEncoder());
            // 加入自己的业务处理 handler TODO
            pipeline.addLast(new GroupChatServerHandler());
          }
        });
      System.out.println("Netty 服务器启动");
      ChannelFuture future = bootstrap.bind(port).sync();
      // 关闭监听
      future.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    new GroupChatServer(11911).run();
  }
}
