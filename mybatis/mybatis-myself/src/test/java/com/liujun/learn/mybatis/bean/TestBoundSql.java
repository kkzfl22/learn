package com.liujun.learn.mybatis.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 测试SQL的解析
 *
 * @author liujun
 * @since 2022/6/28
 */
public class TestBoundSql {

  @Test
  public void runSql() {
    String sql = "select * from user_msg where id = #{id} and name=#{name}";
    BoundSql data = BoundSql.sqlParse(sql);
    Assertions.assertNotNull(data.getSql());
    Assertions.assertNotNull(data.getParameterList());
  }
}
