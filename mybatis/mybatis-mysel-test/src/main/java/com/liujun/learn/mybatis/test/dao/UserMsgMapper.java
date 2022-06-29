package com.liujun.learn.mybatis.test.dao;

import com.liujun.learn.mybatis.test.bean.UserMsgPO;

import java.util.List;

/**
 * 用户查询的接口定义
 *
 * @author liujun
 * @since 2022/6/29
 */
public interface UserMsgMapper {
    /**
     * 查询所有用户信息
     *
     * @return
     */
    List<UserMsgPO> findAllUser();
    /**
     * 按条件查询单个用户的信息
     *
     * @param userQuery 用户的信息
     * @return 结果集
     */
    UserMsgPO findOne(UserMsgPO userQuery);
    /**
     * 添加数据
     *
     * @param user
     * @return
     */
    int insert(UserMsgPO user);
    /**
     * 修改数据
     *
     * @param user
     * @return
     */
    int update(UserMsgPO user);
    /**
     * 删除数据
     *
     * @param user
     * @return
     */
    int delete(UserMsgPO user);
}
