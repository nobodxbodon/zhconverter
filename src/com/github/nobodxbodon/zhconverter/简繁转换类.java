/* Copyright 2017 吴烜 (Xuan Wu)
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nobodxbodon.zhconverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class 简繁转换类 {

  public enum 目标 {
    繁体, 简体
  }
  private final static 简繁转换类 简体转换器 = new 简繁转换类();
  private final static 简繁转换类 繁体转换器 = new 简繁转换类();

  private Properties 字符表 = new Properties();
  private Properties 短语表 = new Properties();
  
  public static 简繁转换类 取实例(目标 简繁) {
    if (简繁.equals(目标.繁体)) {
      繁体转换器.读取字表("简到繁单字.properties", "简到繁短语.properties");
      return 繁体转换器;
    } else {
      简体转换器.读取字表("繁到简单字.properties", "繁到简短语.properties");
      return 简体转换器;
    }
  }
  
  private 简繁转换类() { }
  
  /**
   * 不需自行创建转换器即可转换. 内部调用{@link #转换(String) 转换}方法.
   * @param 文本 任意长度
   * @param 简繁 目标格式
   * @return 转换为目标格式的文本
   */
  public static String 转换(String 文本, 目标 简繁) {
    return 取实例(简繁).转换(文本);
  }

  /**
   * 不进行分词. 如果长度大于1, 寻找匹配的短语. 如没有, 按字寻找对应字后组合.
   * @param 输入文本 任意长度
   * @return 转换后的文本
   */
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

  private void 读取字表(String 单字字表文件名, String 短语字表文件名) {
    读取字表(字符表, 单字字表文件名);
    读取字表(短语表, 短语字表文件名);
  }
  
  private void 读取字表(Properties 对应表, String 属性文件名) {
    InputStream 输入流 = null;
    try {
      输入流 = getClass().getResourceAsStream(属性文件名);
      对应表.load(输入流);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (输入流 != null)
          输入流.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
