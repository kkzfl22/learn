package com.liujun.learn.mybatis.test.bean;

import com.liujun.learn.mybatis.sqlsession.SqlSession;
import com.liujun.learn.mybatis.sqlsession.SqlSessionFactory;
import com.liujun.learn.mybatis.sqlsession.SqlSessionFactoryBuilder;
import com.liujun.learn.mybatis.test.dao.UserMsgMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 测试用户的查询操作
 *
 * @author liujun
 * @since 2022/6/28
 */
public class TestUserMsg {

  @Test
  public void query() throws Exception {
    InputStream mybatisConfig =
        this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = factoryBuilder.builder(mybatisConfig);

    SqlSession sqlSession = sqlSessionFactory.openSession();

    UserMsgPO userQuery = new UserMsgPO();
    userQuery.setId(1);
    userQuery.setName("name");

    List<UserMsgPO> list =
        sqlSession.selectList("com.liujun.learn.mybatis.test.dao.UserMsgMapper.findOne", userQuery);
    System.out.println(list);
    Assertions.assertNotNull(list);
  }

  @Test
  public void queryProxy() throws Exception {
    InputStream mybatisConfig =
        this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = factoryBuilder.builder(mybatisConfig);

    SqlSession sqlSession = sqlSessionFactory.openSession();

    UserMsgMapper userMapper = sqlSession.getMapper(UserMsgMapper.class);

    UserMsgPO userQuery = new UserMsgPO();
    userQuery.setId(1);
    userQuery.setName("name");

    UserMsgPO userRsp = userMapper.findOne(userQuery);
    Assertions.assertNotNull(userRsp);

    List<UserMsgPO> userList = userMapper.findAllUser();
    System.out.println(userList);
    Assertions.assertNotNull(userList);
    Assertions.assertNotEquals(0, userList.size());
  }
}
