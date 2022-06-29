package com.liujun.learn.mybatis.bean;

import com.liujun.learn.mybatis.algorithm.charmatch.bm.CharMatcherBMBadChars;
import com.liujun.learn.mybatis.algorithm.charmatch.bm.CharMatcherBMGoodSuffix;
import com.liujun.learn.mybatis.constant.Symbol;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL的处理对象
 *
 * @author liujun
 * @since 2022/6/27
 */
@Setter
@Getter
@ToString
public class BoundSql {

  /** 开始符号 */
  private static final String START = Symbol.NUMBER_SIGN + Symbol.OPEN_BRACE;

  /** 字符串开始查找对象 */
  private static final CharMatcherBMGoodSuffix MATCH_START = new CharMatcherBMGoodSuffix(START);

  /** 结束符 */
  private static final String END = Symbol.CLOSE_BRACE;

  /** 字符串开始查找对象 */
  private static final CharMatcherBMGoodSuffix MATCH_END = new CharMatcherBMGoodSuffix(END);

  /** SQL对象 */
  private String sql;

  /** SQL的参数对象 */
  private List<BoundParameter> parameterList;

  /**
   * SQL的解析操作
   *
   * @param sql
   * @return
   */
  public static BoundSql sqlParse(String sql) {

    BoundSql sqlInstance = new BoundSql();

    StringBuilder outSql = new StringBuilder();
    byte[] charArrays = CharMatcherBMBadChars.GetBytes(sql);

    // 参数对象
    List<BoundParameter> parameterList = new ArrayList<>();

    int index = -1;
    int startIndex = 0;

    do {
      int leftIndex = MATCH_START.matcherIndex(charArrays, startIndex);
      if (leftIndex == -1) {
        break;
      }
      outSql.append(sql.substring(startIndex, leftIndex));
      outSql.append(Symbol.PROBLEM);
      int rightIndex = MATCH_END.matcherIndex(charArrays, leftIndex);
      String fieldName = sql.substring(leftIndex + START.length(), rightIndex);

      BoundParameter param = new BoundParameter();
      param.setFieldName(fieldName);
      parameterList.add(param);

      startIndex = rightIndex + END.length();

      index++;
    } while (index <= charArrays.length);

    if (outSql.length() <= 0) {
      outSql.append(sql);
    }

    sqlInstance.setSql(outSql.toString());
    sqlInstance.setParameterList(parameterList);

    return sqlInstance;
  }
}
