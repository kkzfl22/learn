package com.liujun.learn.mybatis.dao;

import com.liujun.learn.mybatis.mapper.OrderInfoMapper;
import com.liujun.learn.mybatis.po.OrderPO;
import com.liujun.learn.mybatis.po.UserMsgPO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 测试MyBiatis的基基础用法
 *
 * @author liujun
 * @since 2022/6/30
 */
public class TestUserMsgDao {

  @Test
  public void baseQuery() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    // SqlSession session = sessionFactory.openSession(true);当参数设置为true时，将不再需要手动提交事务.自动提交。
    try (SqlSession session = sessionFactory.openSession(); ) {

      OrderInfoMapper userMsgMapper = session.getMapper(OrderInfoMapper.class);

      // 4, 执行一个查询
      OrderPO dataDataResult = userMsgMapper.selecOrderAndUser(10);
      System.out.println(dataDataResult);
      Assertions.assertNotNull(dataDataResult);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }
}
