package com.liujun.learn.mybatis.sqlsession;

import com.liujun.learn.mybatis.bean.MapperStatement;
import com.liujun.learn.mybatis.bean.MyBatisConfiguration;
import com.liujun.learn.mybatis.constant.OperatorType;
import com.liujun.learn.mybatis.constant.Symbol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author liujun
 * @since 2022/6/27
 */
public class DefaultSqlSession implements SqlSession {

  private final MyBatisConfiguration configuration;

  public DefaultSqlSession(MyBatisConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public <T> List<T> selectList(String statementId, Object... param) {
    // 找到SQL配制对象
    MapperStatement mapperStatement = configuration.getStatementMap().get(statementId);
    // 构建SQL执行器对象
    Executeor executeor = new SimpleExecutor();
    return executeor.query(configuration, mapperStatement, param);
  }

  @Override
  public <T> T selectOne(String statementId, Object... param) {
    // 找到SQL配制对象
    MapperStatement mapperStatement = configuration.getStatementMap().get(statementId);
    // 构建SQL执行器对象
    Executeor executeor = new SimpleExecutor();
    List<T> list = executeor.query(configuration, mapperStatement, param);

    if (null == list || list.isEmpty()) {
      return null;
    }
    if (list.size() > 1) {
      throw new IllegalArgumentException("result more 0");
    }
    return list.get(0);
  }

  @Override
  public int update(String statementId, Object... param) {
    // 找到SQL配制对象
    MapperStatement mapperStatement = configuration.getStatementMap().get(statementId);
    // 构建SQL执行器对象
    Executeor executeor = new SimpleExecutor();
    return executeor.update(configuration, mapperStatement, param);
  }

  @Override
  public <T> T getMapper(Class<?> mapperClass) {

    // 为执行的方法生成代码对象类
    Object proxyInstance =
        Proxy.newProxyInstance(
            this.getClass().getClassLoader(),
            new Class[] {mapperClass},
            new InvocationHandler() {
              @Override
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 1,准备statementId,由于Mapper没有实现类，statementId=接口全限定名+方法名
                String methodName = method.getName();
                String namespace = method.getDeclaringClass().getName();
                String statementId = namespace + Symbol.DOC + methodName;

                MapperStatement mapperStatement = configuration.getStatementMap().get(statementId);

                // 如果当前为修改操作
                if (OperatorType.UPDATE.equals(mapperStatement.getType())) {
                  return update(statementId, args);
                }

                // 2.简单的实现，按参数的类型判断，如果是带有泛型，则调用查询集合方法，否则调用单个查询方法
                Type genericReturnType = method.getGenericReturnType();
                // 如果当前是接口带有泛型，则调用集合方法，否则调用单个查询的方法
                if (genericReturnType instanceof ParameterizedType) {
                  return selectList(statementId, args);
                }

                // 查询单个
                return selectOne(statementId, args);
              }
            });
    return (T) proxyInstance;
  }
}
