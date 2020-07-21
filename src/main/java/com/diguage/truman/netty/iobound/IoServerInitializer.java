package com.diguage.truman.netty.iobound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 19:47
 */
public class IoServerInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    // 入站的 handler 进行编码
    // pipeline.addLast(new ByteToLongDecoder());
    pipeline.addLast(new ByteToLongDecoder2());
    // 出站编码器
    pipeline.addLast(new LongToByteEncoder());
    pipeline.addLast(new IoServerHandler());
    System.out.println(ch);
  }
}
