package com.diguage.truman.netty.chats;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 11:32
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.out.println(msg);
  }
}
