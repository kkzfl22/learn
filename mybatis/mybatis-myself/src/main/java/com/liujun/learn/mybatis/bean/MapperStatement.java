package com.liujun.learn.mybatis.bean;

import com.liujun.learn.mybatis.constant.OperatorType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库操作对象
 *
 * @author liujun
 * @since 2022/6/27
 */
@Getter
@Setter
@ToString
public class MapperStatement {

  /** 类型信息 */
  private OperatorType type;

  /** 操作的id */
  private String id;

  /** 参数的类型 */
  private String parameterType;

  /** 返回值的类型 */
  private String resultType;

  /** SQL的内容信息 */
  private String sqlContext;

  public MapperStatement(Builder builder) {
    this.id = builder.id;
    this.parameterType = builder.parameterType;
    this.resultType = builder.resultType;
    this.sqlContext = builder.sqlContext;
    this.type = builder.type;
  }

  /** 构建器，做SQL的构建操作 */
  public static class Builder {

    /** 类型信息 */
    private OperatorType type;

    /** 操作的id */
    private String id;

    /** 参数的类型 */
    private String parameterType;

    /** 返回值的类型 */
    private String resultType;

    /** SQL的内容信息 */
    private String sqlContext;

    public Builder type(OperatorType type) {
      this.type = type;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder parameterType(String parameterType) {
      this.parameterType = parameterType;
      return this;
    }

    public Builder resultType(String resultType) {
      this.resultType = resultType;
      return this;
    }

    public Builder sqlContext(String sqlContext) {
      this.sqlContext = sqlContext;
      return this;
    }

    /**
     * 执行数据的构建操作
     *
     * @return
     */
    public MapperStatement build() {
      return new MapperStatement(this);
    }
  }

  public static MapperStatement.Builder builder() {
    return new Builder();
  }
}
