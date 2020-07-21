package com.diguage.truman.netty.iobound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 19:55
 */
public class IoServerHandler extends SimpleChannelInboundHandler<Long> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
    System.out.println("IoServerHandler 被调用");
    System.out.println("从客户的" + ctx.channel().remoteAddress() + " 读取long=" + msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.writeAndFlush(120L);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
