package com.diguage.truman.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 17:28
 */
public class NettyServer {
  /**
   * 在 JDK 11 下启动错误： https://stackoverflow.com/a/57892679/951836
   */
  @Test
  public void server() throws InterruptedException {
    // 创建 bossGroup 和 workerGroup
    // 1. 创建两个线程组 bossGroup 和 workerGroup
    // 2. bossGroup 只是处理连接请求，真正的客户端业务处理，会交给 workerGroup 完成
    // 3. 两个都是无限循环
    // 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
    //    默认 CPU 内核数 * 2，在 io.netty.channel.MultithreadEventLoopGroup.DEFAULT_EVENT_LOOP_THREADS 常量中定义
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      // 创建服务器端的启动对象，配置参数
      ServerBootstrap bootstrap = new ServerBootstrap();
      // 使用链式编程进行配置
      bootstrap.group(bossGroup, workerGroup) // 设置两个 线程组
        .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
        .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
        .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
        .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象
          // 给 pipeline 设置处理器
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyServerHandler());
          }
        }); // 给 workerGroup 的 EventLoop 对应的管道设置处理器
      System.out.println("....服务器 is ready...");

      // 绑定一个端口并且同步，生成一个 ChannelFuture 对象
      // 启动服务器（并绑定端口）
      ChannelFuture future = bootstrap.bind(11911).sync();

      // 给 future 注册监听器
      future.addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          if (future.isSuccess()) {
            System.out.println("监听端口 11911 成功");
          } else {
            System.out.println("监听端口 11911 失败");
          }
        }
      });


      // 对关闭通道进行监听
      future.channel().closeFuture().sync();

      // TODO Netty 的异步模型
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
