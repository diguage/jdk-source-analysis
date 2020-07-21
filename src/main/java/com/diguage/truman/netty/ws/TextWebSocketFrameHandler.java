package com.diguage.truman.netty.ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * TextWebSocketFrame 类型，表示一个文本帧
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 15:05
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    System.out.println("服务器收到消息：" + msg.text());
    // 回复消息
    ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间："
      + LocalDateTime.now() + " " + msg.text()));
  }

  /**
   * 当 Web 客户端连接后，出发方法
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
    System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println("发生异常：" + cause.getMessage());
    ctx.channel().close();
  }
}
