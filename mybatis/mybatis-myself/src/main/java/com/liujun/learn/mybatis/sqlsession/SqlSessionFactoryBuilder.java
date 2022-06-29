package com.liujun.learn.mybatis.sqlsession;

import com.liujun.learn.mybatis.bean.MyBatisConfiguration;
import com.liujun.learn.mybatis.process.ConfigParseBuilder;

import java.io.InputStream;

/**
 * 对sqlsession会话对象执行构建
 *
 * @author liujun
 * @since 2022/6/28
 */
public class SqlSessionFactoryBuilder {

  /**
   * 构建会话对象
   *
   * @param input
   * @return
   */
  public SqlSessionFactory builder(InputStream input) throws Exception {
    // 1,做配制文件的解析操作
    ConfigParseBuilder configParseBuilder = new ConfigParseBuilder();
    MyBatisConfiguration configuration = configParseBuilder.readConfig(input);

    // 构建SQL的session工厂处理类
    SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);

    return sqlSessionFactory;
  }
}
