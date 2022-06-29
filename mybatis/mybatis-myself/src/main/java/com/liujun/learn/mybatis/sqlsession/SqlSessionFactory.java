package com.liujun.learn.mybatis.sqlsession;

/**
 * 创建一个会话的工厂类
 *
 * @author liujun
 * @since 2022/6/27
 */
public interface SqlSessionFactory {

  /**
   * 开始一个SQL的会话处理
   *
   * @return
   */
  SqlSession openSession();
}
