package com.diguage.truman.netty.tcprotocol;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 11:09
 */
public class MessageProtocol {
  private int len;
  private byte[] content;

  public int getLen() {
    return len;
  }

  public void setLen(int len) {
    this.len = len;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}
