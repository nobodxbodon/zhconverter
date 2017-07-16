package com.spreada.utils.chinese;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

public class 数据整理类 {

  private static Properties 字符表 = new Properties();

  private static Properties 规整字符表 = new Properties();
  private static 数据整理类 整理 = new 数据整理类();
  
  public static void main(String[] 参数) {
    规整字符表("zh2Hans.properties", "繁到简体字.properties");
    规整字符表("zh2Hant.properties", "简到繁体字.properties");
  }
  
  private static void 规整字符表(String 原字符表文件名, String 输出字符表文件名) {
    字符表.clear();
    规整字符表.clear();
    
    InputStream is = 整理.getClass().getResourceAsStream(原字符表文件名);

    if (is != null) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(is));
        字符表.load(reader);
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
    // 所有单字对应
    Iterator iter = 字符表.keySet().iterator();
    int 添加单字 = 0;
    while (iter.hasNext()) {
      String 文本 = (String) iter.next();
      String 对应文本 = 字符表.getProperty(文本);
      // 去除所有相同单字对应
      if (文本.length() == 1 && !文本.equals(对应文本)) {
        添加单字++;
        规整字符表.setProperty(文本, 对应文本);
      }
    }
    System.out.println("添加单字: " + 添加单字);
    
    // 所有短语的单字对应
    iter = 字符表.keySet().iterator();
    添加单字 = 0;
    while (iter.hasNext()) {
      String 文本 = (String) iter.next();
      if (文本.length() > 1) {
        添加单字 += 拆短语(文本);
      }
    }
    System.out.println("添加短语中单字: " + 添加单字);
    
    // 对所有短语,检查: 如果单字已有对应,则不必要
    iter = 字符表.keySet().iterator();
    int 多余短语数 = 0;
    while (iter.hasNext()) {
      String 文本 = (String) iter.next();
      if (文本.length() > 1) {
        if(多余(文本)) {
          多余短语数 ++;
          //System.out.println(文本);
        } else {
          规整字符表.setProperty(文本, 字符表.getProperty(文本));
        }
      }
    }
    System.out.println(多余短语数);
    
    System.out.println(字符表.size() + " => " + 规整字符表.size());
    
    OutputStream output = null;

    try {
      output = new FileOutputStream(输出字符表文件名);
      规整字符表.store(output, null);
    } catch (IOException io) {
      io.printStackTrace();
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static int 拆短语(String 文本) {
    int 添加单字 = 0;
    String 对应文本 = 字符表.getProperty(文本);
    if (对应文本.length() != 文本.length()) {
      //System.out.println(文本 + "->" + 对应文本);
      return 0;
    }
    for (int 序号 = 0; 序号 < 文本.length(); 序号++) {
      char 字符 = 文本.charAt(序号);
      char 短语中对应字符 = 对应文本.charAt(序号);
      String 对应字符 = 字符表.getProperty(String.valueOf(字符));
      if (对应字符 == null) {
        添加单字++;
        规整字符表.setProperty(String.valueOf(字符), String.valueOf(短语中对应字符));
      } // 如果现有对应字符不包括短语中的
      else if (对应字符.indexOf(短语中对应字符) == -1 && 字符 != 短语中对应字符) {
        添加单字++;
        规整字符表.setProperty(String.valueOf(字符), 对应字符 + String.valueOf(短语中对应字符));
      }
    }
    return 添加单字;
  }

  private static boolean 多余(String 文本) {
    String 单字对应组合 = "";
    String 对应文本 = 字符表.getProperty(文本);
    for (int 序号 = 0; 序号 < 文本.length(); 序号++) {
      char 字符 = 文本.charAt(序号);
      String 对应字符 = 规整字符表.getProperty(String.valueOf(字符));
      if (对应字符 == null) {
        //System.out.println(字符 + " 在: " + 文本);
        return false;
      }
      if (对应字符.length() == 1) {
        单字对应组合 += 对应字符;
      } else {
        //System.out.println("多对应: " + 字符 + "->" + 对应字符);
        return false;
      }
    }
    return 对应文本.equals(单字对应组合);
  }
}
