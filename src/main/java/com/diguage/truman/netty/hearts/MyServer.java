package com.diguage.truman.netty.hearts;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 14:25
 */
public class MyServer {
  public static void main(String[] args) throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO)) // 在 bossGroup 增加一个日志处理器
        .childHandler(new ChannelInitializer<SocketChannel>() {
          /**
           * 加入一个 Netty 提供的 IdleStateHandler
           *
           * 说明：
           *
           * 1. IdleStateHandler 是 Netty 提供的处理空闲状态的处理器
           * 2. long readerIdleTime 表示多长时间没有读取，就会发送一个心跳检查包，检查是否是连接状态
           * 3. long writerIdleTime 表示多长时间没有写，就会发送一个心跳检查包，检查是否是连接状态
           * 4. long allIdleTime 表示多长时间没有读写，就会发送一个心跳检查包，检查是否是连接状态
           *
           * 当 IdleStateHandler 触发后，就会传递给管道的下一个 handler 去处理
           * 通过调用（触发）下一个 handler 的 userEventTriggered，在该方法中处理 IdleStateEvent(读空闲，写空闲，读写空闲)
           */
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // 调整参数，显示不同的事件
            pipeline.addLast(new IdleStateHandler(13, 15, 7, TimeUnit.SECONDS));
            // 加入一个对空闲检测进一步处理的 handler
            pipeline.addLast(new MyServerHandler());
          }
        });

      ChannelFuture future = serverBootstrap.bind(11911).sync();
      future.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
