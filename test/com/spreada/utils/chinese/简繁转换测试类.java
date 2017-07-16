package com.spreada.utils.chinese;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.spreada.utils.chinese.简繁转换类.目标;

public class 简繁转换测试类 {

  private final static 简繁转换类 繁体转换器 = 简繁转换类.getInstance(目标.繁体);
  private final static 简繁转换类 简体转换器 = 简繁转换类.getInstance(目标.简体);
  
  @Test
  public void 基本测试() {
    assertEquals("簡單", 繁体转换器.转换("简单"));
    assertEquals("简单", 简体转换器.转换("簡單"));

    assertEquals("曹操", 繁体转换器.转换("曹操"));
    assertEquals("趙雲", 繁体转换器.转换("赵云"));
    assertEquals("赵云", 简体转换器.转换("趙雲"));
    assertEquals("岳飛", 繁体转换器.转换("岳飞"));
    assertEquals("岳飞", 简体转换器.转换("岳飛"));
    
    // TODO: issue #1. 不知此字繁体是什么?
    assertEquals("暰", 简体转换器.转换("暰"));
    assertEquals("暰", 繁体转换器.转换("暰"));

    // issue #4 简体转繁体时，“机械”一起的时候不能转换，但只有一个"机"字可以转换。
    assertEquals("機", 繁体转换器.转换("机"));
    assertEquals("機械", 繁体转换器.转换("机械"));
    
    // issue #5
    assertEquals("一鬨而散", 繁体转换器.转换("一哄而散"));
    assertEquals("一哄而散", 简体转换器.转换("一鬨而散"));
  }

}
