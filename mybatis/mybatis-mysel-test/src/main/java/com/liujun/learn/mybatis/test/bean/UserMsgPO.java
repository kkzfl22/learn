package com.liujun.learn.mybatis.test.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liujun
 * @since 2022/6/27
 */
@Getter
@Setter
@ToString
public class UserMsgPO {

  /** id的信息 */
  private Integer id;

  /** 名称信息 */
  private String name;
}
