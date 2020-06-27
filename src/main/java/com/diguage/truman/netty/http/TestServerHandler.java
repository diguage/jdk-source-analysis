package com.diguage.truman.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 说明
 * <p>
 * 1. SimpleChannelInboundHandler 就是 ChannelInboundHandlerAdapter 的子类
 * 2. HttpObject 客户端和服务器端相互同学的数据被封装成 HttpObject。
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 23:33
 */
public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {
  /**
   * 读取客户端数据
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    // 判断 msg 是不是一个 HttpRequest 请求
    if (msg instanceof HttpRequest) {

      System.out.println("pipeline hashcode=" + ctx.pipeline().hashCode()
        + " TestServerHandler hash=" + this.hashCode());

      System.out.println("msg 类型 " + msg.getClass());
      System.out.println("客户端地址 " + ctx.channel().remoteAddress());

      HttpRequest httpRequest = (HttpRequest) msg;
      URI uri = new URI(httpRequest.uri());
      if ("/favicon.ico".equals(uri.getPath())) {
        System.out.println("请求了 favicon.ico，不做响应");
        return;
      }

      // 回复信息给浏览器(http协议)
      ByteBuf content = Unpooled.copiedBuffer("Hello, D瓜哥。我是服务器", UTF_8);
      FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

      // 将构建好的 response 返回
      ctx.writeAndFlush(response);
    }
  }
}
