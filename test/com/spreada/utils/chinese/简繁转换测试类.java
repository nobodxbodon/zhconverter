package com.spreada.utils.chinese;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.spreada.utils.chinese.简繁转换类.目标;

public class 简繁转换测试类 {

  @Test
  public void 静态方法测试() {
    确认简繁体互转("简单", "簡單");
    确认简繁体互转("曹操", "曹操");
    确认简繁体互转("赵云", "趙雲");
    确认简繁体互转("岳飞", "岳飛");

    // TODO: issue #1. 不知此字繁体是什么?
    确认简繁体互转("暰", "暰");
    
    // issue #4 简体转繁体时，“机械”一起的时候不能转换，但只有一个"机"字可以转换。
    确认简繁体互转("机", "機");
    确认简繁体互转("机械", "機械");

    // issue #5
    确认简繁体互转("一哄而散", "一鬨而散");
    
    确认简繁体互转("有背光的机械式键盘", "有背光的機械式鍵盤");
  }
  
  @Test
  public void 基本转换测试() {
    final 简繁转换类 繁体转换器 = 简繁转换类.取实例(目标.繁体);
    final 简繁转换类 简体转换器 = 简繁转换类.取实例(目标.简体);
    
    assertEquals("簡單", 繁体转换器.转换("简单"));
    assertEquals("简单", 简体转换器.转换("簡單"));
    
    // 如果已是简体, 简体转换后不变; 繁体亦然
    assertEquals("簡單", 繁体转换器.转换("簡單"));
    assertEquals("简单", 简体转换器.转换("简单"));
  }

  @Test
  public void 边界测试() {
    确认简繁体互转("", "");
  }
  
  private void 确认简繁体互转(String 简体文本, String 繁体文本) {
    assertEquals(繁体文本, 简繁转换类.转换(简体文本, 目标.繁体));
    assertEquals(简体文本, 简繁转换类.转换(繁体文本, 目标.简体));
  }
}
