package com.diguage.truman.netty.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 14:56
 */
public class Server {
  public static void main(String[] args) throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO)) // 在 bossGroup 增加一个日志处理器
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // 因为基于 HTTP 协议，使用 HTTP 的编解码器
            pipeline.addLast(new HttpServerCodec());
            // 是以块方法写，加 ChunkedWriteHandler 处理器
            pipeline.addLast(new ChunkedWriteHandler());
            /**
             * 说明
             * 1. HTTP 数据在传输过程中是分段的，HttpObjectAggregator 就可以将多个段聚合
             * 2. 这就是为什么，当浏览器发送大量数据时，就会发出多次 HTTP 请求
             */
            pipeline.addLast(new HttpObjectAggregator(8192));
            /**
             * 说明
             * 1. 对应 WebSocket，它的数据是以帧(frame)形式传递
             * 2. 可以看到 WebSocketFrame 下面有六个子类
             * 3. 浏览器请求时： ws://localhost:11911/hello
             * 4. WebSocketServerProtocolHandler 核心功能是将 HTTP 协议升级为 WS 协议，保持长连接
             *    这一点可以观察浏览器的链接信息，可以看到协议升级的过程。
             */
            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

            // 自定义 Handler，处理业务逻辑
            pipeline.addLast(new TextWebSocketFrameHandler());
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
