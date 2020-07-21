package com.diguage.truman.netty.tcprotocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:00
 */
public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new MessageEncoder());// TODO 必须放在 handler 上面吗？
    pipeline.addLast(new MessageDecoder());
    pipeline.addLast(new TcpClientHandler());
  }
}
