package com.diguage.truman.netty.tcprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 11:18
 */
public class MessageDecoder extends ReplayingDecoder<Void> {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    System.out.println("\n\nMessageDecoder decode 被调用");
    // 需要将得到的二进制字节码 -> MessageProtocol 数据包
    int len = in.readInt();
    byte[] bytes = new byte[len];
    in.readBytes(bytes);
    // 封装成 MessageProtocol 对象，放入out，传递给下一个 handler 业务处理
    MessageProtocol msp = new MessageProtocol();
    msp.setLen(len);
    msp.setContent(bytes);
    out.add(msp);
  }
}
