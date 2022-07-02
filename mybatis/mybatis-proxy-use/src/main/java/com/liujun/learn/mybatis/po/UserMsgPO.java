package com.liujun.learn.mybatis.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户对象
 *
 * @author liujun
 * @since 2022/6/29
 */
@Setter
@Getter
@ToString
public class UserMsgPO {

  /** 用户的id */
  private Integer id;

  /** 名称的信息 */
  private String name;
}
