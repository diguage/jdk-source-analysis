package com.diguage.truman.netty.dubbo.netty;

import com.diguage.truman.netty.dubbo.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 17:03
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // 获取客户端发送的消息，并调用服务
    System.out.println("msg=" + msg);
    // 客户端在调用服务器的 API 时，需要定义一个协议
    // 比如每次发消息时，都必须以某个字符串开头 "#Hello#"
    String prefix = "#Hello#";
    if (msg.toString().startsWith(prefix)) {
      String result = new HelloServiceImpl().hello(msg.toString().substring(prefix.length()));
      ctx.writeAndFlush(result);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
  }
}
