package com.diguage.truman.netty.iobound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:00
 */
public class IoClientInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    // 加入一个出站的 handler，对数据进行一个编码
    pipeline.addLast(new LongToByteEncoder());
    // 入站解码器
    // pipeline.addLast(new ByteToLongDecoder());
    pipeline.addLast(new ByteToLongDecoder2());
    pipeline.addLast(new IoClientHandler());
  }
}
