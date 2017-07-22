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

import java.util.ResourceBundle;

/**
 * 字库基于原项目https://code.google.com/archive/p/java-zhconverter/, 据项目描述来源于MediaWiki.
 * <p>转换规则很简单, 完全不进行分词.
 * <p>如果输入文本不是单字, 如果在对应表中有完全匹配, 就返回对应的文本; 不然就逐字按照单字转换.
 */
public class 简繁转换类 {

  public enum 目标 {
    繁体, 简体
  }
  private final static 简繁转换类 简体转换器 = new 简繁转换类();
  private final static 简繁转换类 繁体转换器 = new 简繁转换类();

  private ResourceBundle 对应表 = null;
  
  public static 简繁转换类 取实例(目标 简繁) {
    if (简繁.equals(目标.繁体)) {
      繁体转换器.对应表 = ResourceBundle.getBundle("简到繁单字");
      return 繁体转换器;
    } else {
      简体转换器.对应表 = ResourceBundle.getBundle("繁到简单字");
      return 简体转换器;
    }
  }
  
  private 简繁转换类() { }
  
  /**
   * 不需自行创建转换器即可转换. 内部调用{@link #转换(String) 转换}方法.
   * @param 文本 任意长度
   * @param 简繁 目标格式
   * @return 转换为目标格式的文本
   * @throws IllegalArgumentException 文本为null时
   */
  public static String 转换(String 文本, 目标 简繁) {
    return 取实例(简繁).转换(文本);
  }

  /**
   * 不进行分词. 如果长度大于1, 寻找匹配的短语. 如没有, 按字寻找对应字后组合.
   * @param 输入文本 任意长度
   * @return 转换后的文本
   * @throws IllegalArgumentException 文本为null时
   */
  public String 转换(String 输入文本) {
    if (输入文本 == null) {
      throw new IllegalArgumentException("字符串为null");
    }

    StringBuilder 输出文本器 = new StringBuilder();

    if (输入文本.length() > 1 && 对应表.containsKey(输入文本)) {
      return 对应表.getString(输入文本);
    }

    for (char 字符 : 输入文本.toCharArray()){
      String 单字 = String.valueOf(字符);

      // 如有多个对应字符, 暂时用第一个; 如果没有对应字符, 保留原字符
      输出文本器.append(对应表.containsKey(单字) ? 对应表.getString(单字).charAt(0) : 单字);
    }
    return 输出文本器.toString();
  }
}
