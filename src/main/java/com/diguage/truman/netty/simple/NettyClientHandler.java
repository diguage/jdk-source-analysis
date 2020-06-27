package com.diguage.truman.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 19:41
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当通道就绪就会触发该方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, D瓜哥，ping -> \\(^o^)/", UTF_8));
    }

    /**
     * 当通道有读取事件时，会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("");
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(UTF_8));
        System.out.println("服务器的地址：" + ctx.channel().remoteAddress());
    }
}
