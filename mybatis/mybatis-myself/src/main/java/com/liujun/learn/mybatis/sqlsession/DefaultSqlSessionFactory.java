package com.liujun.learn.mybatis.sqlsession;

import com.liujun.learn.mybatis.bean.MyBatisConfiguration;

/**
 * 默认的工厂实现类
 *
 * @author liujun
 * @since 2022/6/27
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

  private final MyBatisConfiguration configuration;

  public DefaultSqlSessionFactory(MyBatisConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public SqlSession openSession() {
    return new DefaultSqlSession(configuration);
  }
}
