package com.liujun.learn.mybatis.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单的信息
 *
 * @author liujun
 * @since 2022/6/29
 */
@Setter
@Getter
@ToString
public class OrderPO {

  /** 用户的订单的主键的id */
  private Integer id;

  /** 订单的金额 */
  private Integer money;

  /** 订单的时间 */
  private Long orderTime;

  /** 用户的id */
  private Integer userid;

  /** 订单的用户信息 */
  private UserMsgPO user;
}
