package com.liujun.learn.mybatis.sqlsession;

import java.util.List;

/**
 * 执行SQL的相关查询操作
 *
 * @author liujun
 * @since 2022/6/27
 */
public interface SqlSession {

  /**
   * 执行查询操作
   *
   * @param statementId 指向的SQL的id
   * @param param 运行SQL的参数信息
   * @param <T> 查询的的结果集的类型
   * @return 返回结果集的类型
   */
  <T> List<T> selectList(String statementId, Object... param);

  /**
   * 执行查询操作
   *
   * @param statementId 指向的SQL的id
   * @param param 运行SQL的参数信息
   * @param <T> 查询的的结果集的类型
   * @return 返回结果集的类型
   */
  <T> T selectOne(String statementId, Object... param);

  /**
   * 为dao的接口生成代理实现类
   *
   * @param mapperClass 代理的对象类
   * @param <T> 查询的结果集
   */
  <T> T getMapper(Class<?> mapperClass);
}
