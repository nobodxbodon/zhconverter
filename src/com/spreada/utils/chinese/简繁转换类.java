package com.spreada.utils.chinese;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class 简繁转换类 {

  private Properties 字符表 = new Properties();
  private Properties 短语表 = new Properties();
  
  public enum 目标 {
    繁体, 简体;
  }
  private final static 简繁转换类 简体转换器 = new 简繁转换类();
  private final static 简繁转换类 繁体转换器 = new 简繁转换类();

  public static 简繁转换类 getInstance(目标 简繁) {
    if (简繁.equals(目标.繁体)) {
      繁体转换器.读取字表("简到繁单字.properties", "简到繁短语.properties");
      return 繁体转换器;
    } else {
      简体转换器.读取字表("繁到简单字.properties", "繁到简短语.properties");
      return 简体转换器;
    }
  }
  
  private 简繁转换类() { }
  
  public static String 转换(String 文本, 目标 简繁) {
    简繁转换类 instance = getInstance(简繁);
    return instance.转换(文本);
  }

  private void 读取字表(String 单字字表文件名, String 短语字表文件名) {
    读取字表(字符表, 单字字表文件名);
    读取字表(短语表, 短语字表文件名);
  }
  
  private void 读取字表(Properties 对应表, String 属性文件名) {
    InputStream is = getClass().getResourceAsStream(属性文件名);

    if (is != null) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(is));
        对应表.load(reader);
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          if (reader != null)
            reader.close();
          if (is != null)
            is.close();
        } catch (IOException e) {
        }
      }
    }
  }

  // 不进行分词: 如果短语没有匹配,则按字寻找对应后组合
  public String 转换(String 输入文本) {
    StringBuilder 输出文本器 = new StringBuilder();

    if (输入文本.length() > 1 && 短语表.containsKey(输入文本)) {
      return 短语表.getProperty(输入文本);
    }
    
    for (int i = 0; i < 输入文本.length(); i++) {
      String 单字 = String.valueOf(输入文本.charAt(i));
      
      // 如有多个对应字符, 暂时用第一个; 如果没有对应字符, 保留原字符
      输出文本器.append(字符表.containsKey(单字) ? 字符表.getProperty(单字).charAt(0) : 单字);
    }
    return 输出文本器.toString();
  }
}
