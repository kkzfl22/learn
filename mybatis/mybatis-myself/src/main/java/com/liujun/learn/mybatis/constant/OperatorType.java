package com.liujun.learn.mybatis.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 操作的类型
 *
 * @author liujun
 * @since 2022/6/29
 */
@Getter
@ToString
public enum OperatorType {

  /** 查询 */
  QUERY("query"),

  /** 添加、修改、删除 */
  UPDATE("update"),
  ;

  private String type;

  OperatorType(String type) {
    this.type = type;
  }
}
