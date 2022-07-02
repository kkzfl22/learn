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
   * 添加数据
   *
   * @param userMsg 存储的实体信息
   * @return
   */
  int insertUser(UserMsgPO userMsg);

  /**
   * 修改数据
   *
   * @param userMsgPO
   * @return
   */
  int updateUser(UserMsgPO userMsgPO);

  /**
   * 删除数据
   *
   * @param id
   * @return
   */
  int deleteUser(Integer id);

  /**
   * 查询的所有擞 据
   *
   * @return
   */
  List<UserMsgPO> selectAll();

  /**
   * 查询的单个数据
   *
   * @return
   */
  UserMsgPO selectOne(UserMsgPO userMsg);
}
