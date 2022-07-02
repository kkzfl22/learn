package com.liujun.learn.mybatis.mapper;

import com.liujun.learn.mybatis.po.OrderPO;
import com.liujun.learn.mybatis.po.UserMsgPO;

import java.util.List;

/**
 * 用户的Mapper的操作
 *
 * @author liujun
 * @since 2022/7/1
 */
public interface OrderInfoMapper {

  /**
   * 通过订单号来查询订单和用户信息
   *
   * @return
   */
  OrderPO selecOrderAndUser(Integer orderId);
}
