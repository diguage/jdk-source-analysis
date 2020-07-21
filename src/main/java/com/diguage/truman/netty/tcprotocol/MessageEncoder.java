package com.diguage.truman.netty.tcprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 11:15
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
  @Override
  protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
    System.out.println("MessageEncoder encode 方法被调用");
    out.writeInt(msg.getLen());
    out.writeBytes(msg.getContent());
  }
}
