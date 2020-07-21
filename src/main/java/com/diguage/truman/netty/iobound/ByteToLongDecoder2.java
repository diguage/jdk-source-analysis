package com.diguage.truman.netty.iobound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 19:53
 */
public class ByteToLongDecoder2 extends ReplayingDecoder<Void> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // 在 ReplayingDecoder 不需要判断数据是否足够读取，内部会进行处理判断。
    out.add(in.readLong());
  }
}
