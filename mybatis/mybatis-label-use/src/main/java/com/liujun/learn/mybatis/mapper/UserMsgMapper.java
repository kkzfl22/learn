package com.liujun.learn.mybatis.mapper;

import com.liujun.learn.mybatis.po.UserMsgPO;

import java.util.List;

/**
 * 用户的Mapper的操作
 *
 * @author liujun
 * @since 2022/7/1
 */
public interface UserMsgMapper {

  /**
   * 查询的单个数据
   *
   * @return
   */
  UserMsgPO selectOne(UserMsgPO userMsg);

  /**
   * 查询的单个数据
   *
   * @return
   */
  List<UserMsgPO> selectForcach(List<Integer> userIdList);

  /**
   * 查询的单个数据
   *
   * @return
   */
  List<UserMsgPO> selectForcachArray(int[] userIdArray);
}
