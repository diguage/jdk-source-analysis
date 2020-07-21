package com.diguage.truman.netty.iobound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:02
 */
public class LongToByteEncoder extends MessageToByteEncoder<Long> {
  @Override
  protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
    System.out.println("LongToByteEncoder encode 被调用");
    System.out.println("msg=" + msg);
    out.writeLong(msg);
  }
}
