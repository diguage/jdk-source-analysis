package com.diguage.truman.netty.iobound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 19:53
 */
public class ByteToLongDecoder extends ByteToMessageDecoder {
  /**
   * decode 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到 list 或者 `ByteBuf` 没有更多的可读字节为止。
   * <p>
   * 如果 list out 不为空，就会将 List 的内容传递给下一个 ChannelInboundHandler 处理，该处理器方法也会被调用多次。
   *
   * @param ctx 上下文对象
   * @param in  入站的 ByteBuf
   * @param out List 集合，将解码后的数据传给下一个 handler
   * @throws Exception
   */
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    System.out.println("ByteToLongDecoder decode 被调用");
    if (in.readableBytes() >= 8) {
      out.add(in.readLong());
    }
  }
}
