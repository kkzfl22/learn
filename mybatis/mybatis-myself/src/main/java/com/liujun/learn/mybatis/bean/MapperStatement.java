package com.liujun.learn.mybatis.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库操作对象
 *
 * @author liujun
 * @since 2022/6/27
 */
@Getter
@Setter
@ToString
public class MapperStatement {

  /** 操作的id */
  private String id;

  /** 参数的类型 */
  private String parameterType;

  /** 返回值的类型 */
  private String resultType;

  /** SQL的内容信息 */
  private String sqlContext;

  public MapperStatement(String id, String parameterType, String resultType, String sqlContext) {
    this.id = id;
    this.parameterType = parameterType;
    this.resultType = resultType;
    this.sqlContext = sqlContext;
  }
}
