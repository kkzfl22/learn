package com.liujun.learn.mybatis.sqlsession;

import com.liujun.learn.mybatis.bean.MapperStatement;
import com.liujun.learn.mybatis.bean.MyBatisConfiguration;

import java.util.List;

/**
 * SQL执行的工厂类
 *
 * @author liujun
 * @since 2022/6/27
 */
public interface Executeor {

  /**
   * 通用的查询的接口
   *
   * @param config 配制的信息
   * @param statement 当前所有运行的SQL对象
   * @param param 运行的参数信息
   * @param <E> 查询的结果集对象
   * @return 查询的结果集
   */
  <E> List<E> query(MyBatisConfiguration config, MapperStatement statement, Object... param);
}
