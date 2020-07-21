package com.diguage.truman.netty.iobound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:05
 */
public class IoClientHandler extends SimpleChannelInboundHandler<Long> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
    System.out.println("从服务器接受消息， msg=" + msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("IoClientHandler 发送数据");
    //  ctx.writeAndFlush(119L); // 发送一个 long
    // 注意：这个实验数据！！！
    // 分析
    // 1. "abcdabcdabcdabcd" 是 16 个字节
    // 2. 该处理器的前一个 handler 是 LongToByteEncoder
    // 3. LongToByteEncoder 父类是 MessageToByteEncoder
    // 4. 父类的 write 调用 acceptOutboundMessage 方法来检查是不是可以接受的数据，
    //    是则执行子类 encode 的方法，否则直接传递到下一个 handler
    // 因此，在编写 Enoder 时，要注意传入的数据类型和处理的数据类型一致。否则就跳过由下一个handler处理了。
    ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", UTF_8));
  }
}
