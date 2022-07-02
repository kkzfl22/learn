package com.liujun.learn.mybatis.dao;

import com.liujun.learn.mybatis.mapper.UserMsgMapper;
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
  public void testAllInOne() {
    this.baseInsert();
    this.baseUpdate();
    this.baseQuery();
    this.baseQueryOne();
    this.baseDelete();
  }

  public void baseQuery() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    // SqlSession session = sessionFactory.openSession(true);当参数设置为true时，将不再需要手动提交事务.自动提交。
    try (SqlSession session = sessionFactory.openSession(); ) {

      UserMsgMapper userMsgMapper = session.getMapper(UserMsgMapper.class);

      // 4, 执行一个查询
      List<UserMsgPO> queryList = userMsgMapper.selectAll();

      for (UserMsgPO msg : queryList) {
        System.out.println(msg);
      }

      Assertions.assertNotEquals(0, queryList.size());
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  public void baseQueryOne() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    // SqlSession session = sessionFactory.openSession(true);当参数设置为true时，将不再需要手动提交事务.自动提交。
    try (SqlSession session = sessionFactory.openSession(); ) {

      UserMsgMapper userMsgMapper = session.getMapper(UserMsgMapper.class);

      UserMsgPO userQuery = new UserMsgPO();
      userQuery.setId(5);

      // 4, 执行一个查询
      UserMsgPO userData = userMsgMapper.selectOne(userQuery);

      Assertions.assertNotNull(userData);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  public void baseInsert() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    try (SqlSession session = sessionFactory.openSession(); ) {

      UserMsgPO userDataSave = new UserMsgPO();
      userDataSave.setId(5);
      userDataSave.setName("name");

      UserMsgMapper userMsgMapper = session.getMapper(UserMsgMapper.class);
      // 4, 执行一个添加操作
      int insert = userMsgMapper.insertUser(userDataSave);
      session.commit();
      Assertions.assertNotEquals(0, insert);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  public void baseUpdate() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    try (SqlSession session = sessionFactory.openSession(); ) {

      UserMsgPO userUpdate = new UserMsgPO();
      userUpdate.setId(5);
      userUpdate.setName("name5");

      UserMsgMapper userMsgMapper = session.getMapper(UserMsgMapper.class);

      // 4, 执行一个修改操作
      int update = userMsgMapper.updateUser(userUpdate);
      session.commit();
      Assertions.assertNotEquals(0, update);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  public void baseDelete() {
    // 1,加载Mybatis的核心配制文件为流
    InputStream mapConfigStream =
        this.getClass().getClassLoader().getResourceAsStream("sqlMapConfig.xml");

    // 2,构建一个SqlSessionFactory对象
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mapConfigStream);

    // 3,打开一个Session对象
    try (SqlSession session = sessionFactory.openSession(); ) {

      UserMsgMapper userMsgMapper = session.getMapper(UserMsgMapper.class);

      // 4, 执行一个删除操作
      int delete = userMsgMapper.deleteUser(5);
      session.commit();
      Assertions.assertNotEquals(0, delete);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }
}
