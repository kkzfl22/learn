package com.liujun.learn.mybatis.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SQL的参数对象信息
 *
 * @author liujun
 * @since 2022/6/27
 */
@Getter
@Setter
@ToString
public class BoundParameter {

  /** SQL的属性的名称 */
  private String fieldName;
}
