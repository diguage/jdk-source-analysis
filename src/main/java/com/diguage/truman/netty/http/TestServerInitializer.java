package com.diguage.truman.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 23:33
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    // 向管道加入处理器
    // 得到管道
    ChannelPipeline pipeline = ch.pipeline();
    // 加入一个 Netty 提供的 HttpServerCodec
    // HttpServerCodec 的说明
    // 1. HttpServerCodec 是 Netty 提供的处理 HTTP 的编解码器
    pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
    // 2. 增加一个自定义的 Handler
    pipeline.addLast("MyTestServerHandler", new TestServerHandler());

    System.out.println(pipeline);
  }
}
