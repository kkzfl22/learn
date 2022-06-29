package com.liujun.learn.mybatis.sqlsession;

import cn.hutool.core.io.IoUtil;
import com.liujun.learn.mybatis.bean.BoundParameter;
import com.liujun.learn.mybatis.bean.BoundSql;
import com.liujun.learn.mybatis.bean.MapperStatement;
import com.liujun.learn.mybatis.bean.MyBatisConfiguration;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的SQL对象执行器
 *
 * @author liujun
 * @since 2022/6/27
 */
public class SimpleExecutor implements Executeor {

  @Override
  public <E> List<E> query(
      MyBatisConfiguration config, MapperStatement mapStatement, Object... param) {
    List<E> resultList = null;
    PreparedStatement sqlStatement = null;
    ResultSet rs = null;
    try {
      // 1,获取数据库的连接
      Connection conn = config.getDataSource().getConnection();
      // 2,执行对SQL的转换操作
      BoundSql sqlProc = BoundSql.sqlParse(mapStatement.getSqlContext());
      // 3.生成SQL运行对象
      sqlStatement = conn.prepareStatement(sqlProc.getSql());

      // 4 进行参数的填充
      for (int i = 0; i < sqlProc.getParameterList().size(); i++) {
        BoundParameter parameter = sqlProc.getParameterList().get(i);
        Class paramType = Class.forName(mapStatement.getParameterType());
        Field field = paramType.getDeclaredField(parameter.getFieldName());
        field.setAccessible(Boolean.TRUE);
        sqlStatement.setObject(i + 1, field.get(param[0]));
      }
      resultList = new ArrayList<>();

      // 5. 执行查询,并完成响应值的封装
      rs = sqlStatement.executeQuery();
      Class result = Class.forName(mapStatement.getResultType());
      while (rs.next()) {
        // 响应对象
        Object resultInstance = result.newInstance();
        ResultSetMetaData metadata = rs.getMetaData();
        // 列的访问顺序从1开始
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
          String columnName = metadata.getColumnName(i);
          Object columnValue = rs.getObject(columnName);
          //// 使用反射完成对象的封装,例用反射分出现
          Field resultField = result.getDeclaredField(columnName);
          resultField.setAccessible(Boolean.TRUE);
          resultField.set(resultInstance, columnValue);

          //// 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
          // PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, result);
          // Method writeMethod = propertyDescriptor.getWriteMethod();
          // writeMethod.invoke(resultInstance, columnValue);
        }

        resultList.add((E) resultInstance);
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } finally {
      IoUtil.close(rs);
      IoUtil.close(sqlStatement);
    }

    return resultList;
  }

  @Override
  public int update(MyBatisConfiguration config, MapperStatement statement, Object... param) {
    int updateNum = 0;
    PreparedStatement sqlStatement = null;
    try {
      // 1,获取数据库的连接
      Connection conn = config.getDataSource().getConnection();
      // 2,执行对SQL的转换操作
      BoundSql sqlProc = BoundSql.sqlParse(statement.getSqlContext());
      // 3.生成SQL运行对象
      sqlStatement = conn.prepareStatement(sqlProc.getSql());

      // 4 进行参数的填充
      for (int i = 0; i < sqlProc.getParameterList().size(); i++) {
        BoundParameter parameter = sqlProc.getParameterList().get(i);
        Class paramType = Class.forName(statement.getParameterType());
        Field field = paramType.getDeclaredField(parameter.getFieldName());
        field.setAccessible(Boolean.TRUE);
        sqlStatement.setObject(i + 1, field.get(param[0]));
      }

      updateNum = sqlStatement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } finally {
      IoUtil.close(sqlStatement);
    }

    return updateNum;
  }
}
