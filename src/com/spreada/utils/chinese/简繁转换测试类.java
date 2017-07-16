package com.spreada.utils.chinese;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class 简繁转换测试类 {

  private final static 简繁转换类 繁体转换器 = 简繁转换类.getInstance(简繁转换类.TRADITIONAL);
  private final static 简繁转换类 简体转换器 = 简繁转换类.getInstance(简繁转换类.SIMPLIFIED);
  
  @Test
  public void 基本测试() {
    assertEquals("簡單", 繁体转换器.convert("简单"));
    assertEquals("简单", 简体转换器.convert("簡單"));
  }

}
