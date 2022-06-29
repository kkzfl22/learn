package com.liujun.learn.mybatis.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 配制文件的信息
 *
 * @author liujun
 * @since 2022/6/27
 */
@Getter
@Setter
@ToString
public class MyBatisConfiguration {

  /** 连接池的信息 */
  private DataSource dataSource;

  /** SQL的配制信息 */
  private Map<String, MapperStatement> statementMap;
}
