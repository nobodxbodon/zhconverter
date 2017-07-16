package com.spreada.utils.chinese;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.spreada.utils.chinese.简繁转换类;
import com.spreada.utils.chinese.简繁转换类.目标;

public class 简繁转换测试类 {

  private final static 简繁转换类 繁体转换器 = 简繁转换类.getInstance(目标.繁体);
  private final static 简繁转换类 简体转换器 = 简繁转换类.getInstance(目标.简体);
  
  @Test
  public void 基本测试() {
    assertEquals("簡單", 繁体转换器.转换("简单"));
    assertEquals("简单", 简体转换器.转换("簡單"));
  }

}
