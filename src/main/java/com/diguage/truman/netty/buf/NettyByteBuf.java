package com.diguage.truman.netty.buf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 10:26
 */
public class NettyByteBuf {
  @Test
  public void test01() {
    // 1. 创建对象，对象包含一个 byte[10] 的数组
    // 2. 在 Netty 的 ByteBuf 中，不需要使用 flip 进行反转
    //    底层维护了 readIndex 和 writeIndex
    // 3. 通过 readIndex、 writeIndex 和 capacity，将 buffer 分成三个区域
    //    3.1 0 -- readIndex 已读
    //    3.2 readIndex - writeIndex 可读
    //    3.3 writeIndex - capacity 可写
    ByteBuf buffer = Unpooled.buffer(10);
    for (int i = 0; i < 10; i++) {
      buffer.writeByte(i);
    }

    System.out.println("capacity=" + buffer.capacity());
    // 输出
    for (int i = 0; i < buffer.capacity(); i++) {
      System.out.println(buffer.getByte(i));
    }

    for (int i = 0; i < buffer.capacity(); i++) {
      System.out.println(buffer.readByte());
    }
  }

  @Test
  public void test02() {
    // 创建 ByteBuf
    ByteBuf byteBuf = Unpooled.copiedBuffer("Hello, D瓜哥！", UTF_8);

    // 使用相关方法
    if (byteBuf.hasArray()) {
      byte[] content = byteBuf.array();
      // 将 content 转成字符串
      System.out.println(new String(content, UTF_8));

      System.out.println("byteBuf=" + byteBuf);
      System.out.println(byteBuf.arrayOffset());
      System.out.println(byteBuf.readerIndex());
      System.out.println(byteBuf.writerIndex());
      System.out.println(byteBuf.capacity());
      System.out.println(byteBuf.readByte());

      int len = byteBuf.readableBytes(); // 可读取的字符数
      System.out.println(len);

      for (int i = 0; i < len; i++) {
        System.out.println((char) byteBuf.getByte(i));
      }

      // 按照范围读取
      System.out.println(byteBuf.getCharSequence(0, 4, UTF_8));
    }
  }


}
