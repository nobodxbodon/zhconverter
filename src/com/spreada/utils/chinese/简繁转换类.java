package com.spreada.utils.chinese;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class 简繁转换类 {

  private Properties 字符表 = new Properties();
  private Set<String> 争议集 = new HashSet<>();

  public enum 目标 {
    繁体(0), 简体(1);
    
    int 内部值;
    目标(int 值) {
      内部值 = 值;
    }
  }
  private static final int TRADITIONAL = 0;
  private static final int SIMPLIFIED = 1;
  private static final int NUM_OF_CONVERTERS = 2;
  private static final 简繁转换类[] converters = new 简繁转换类[NUM_OF_CONVERTERS];
  private static final String[] propertyFiles = new String[2];

  static {
    propertyFiles[TRADITIONAL] = "繁体字.properties";
    propertyFiles[SIMPLIFIED] = "简体字.properties";
  }

  public static 简繁转换类 getInstance(目标 简繁) {
    return getInstance(简繁.内部值);
  }
  
  /**
   *
   * @param converterType 0 for traditional and 1 for simplified
   * @return
   */
  private static 简繁转换类 getInstance(int converterType) {
    if (converters[converterType] == null) {
      synchronized (简繁转换类.class) {
        converters[converterType] = new 简繁转换类(propertyFiles[converterType]);
      }
    }
    return converters[converterType];
  }

  public static String 转换(String 文本, 目标 简繁) {
    简繁转换类 instance = getInstance(简繁);
    return instance.转换(文本);
  }


  private 简繁转换类(String propertyFile) {
    InputStream is = getClass().getResourceAsStream(propertyFile);

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
    initializeHelper();
  }

  // TODO: 由于短语有重复部分,争议集里有很大的水分
  private void initializeHelper() {
    Map<String, Integer> 对应字符个数 = new HashMap<>();
    Iterator iter = 字符表.keySet().iterator();
    while (iter.hasNext()) {
      String 文本 = (String) iter.next();
      if (文本.length() >= 1) {
        for (int i = 0; i < 文本.length(); i++) {
          String 子文本 = 文本.substring(0, i + 1);
          if (对应字符个数.containsKey(子文本)) {
            对应字符个数.put(子文本, new Integer(对应字符个数.get(子文本) + 1));
          } else {
            对应字符个数.put(子文本, new Integer(1));
          }
        }
      }
    }

    iter = 对应字符个数.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      if ((对应字符个数.get(key)).intValue() > 1) {
        争议集.add(key);
      }
    }
  }

  public String 转换(String in) {
    StringBuilder outString = new StringBuilder();
    StringBuilder stackString = new StringBuilder();

    for (int i = 0; i < in.length(); i++) {
      char c = in.charAt(i);
      String key = "" + c;
      stackString.append(key);

      if (争议集.contains(stackString.toString())) {
        // TODO: 不处理?
      } else if (字符表.containsKey(stackString.toString())) {
        outString.append(字符表.get(stackString.toString()));
        stackString.setLength(0);
      } else {
        CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
        stackString.delete(0, stackString.length() - 1);
        flushStack(outString, new StringBuilder(sequence));
      }
    }

    flushStack(outString, stackString);

    return outString.toString();
  }

  private void flushStack(StringBuilder outString, StringBuilder stackString) {
    while (stackString.length() > 0) {
      if (字符表.containsKey(stackString.toString())) {
        outString.append(字符表.get(stackString.toString()));
        stackString.setLength(0);
      } else {
        outString.append("" + stackString.charAt(0));
        stackString.delete(0, 1);
      }
    }
  }

  String parseOneChar(String c) {
    return 字符表.containsKey(c) ? (String) 字符表.get(c) : c;
  }
}
