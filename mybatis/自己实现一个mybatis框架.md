## 自已实现一个mybatis框架

>
>使用了mybatis框架这么久了，对原理也没有深入的理解，正好有此机会，对mybatis的源码做一个深入的分析，在分析之前，
>自己先实现一个简单版本的mybatis框架，然后再阅读源码，这样就能对源码，有更多的深入了解。
>
>开始自己的mybatis框架吧。
>


### 1. 环境准备-数据库-sqllite

```sql

-- 用户信息
drop table if exists  user_msg;

create table user_msg
(
    id                            	int(8)          not null ,
    name                            varchar(100)        not null,
    primary key (id)
) ;


insert into user_msg(id,name)
values(1,'name');

insert into user_msg(id,name)
values(2,'name2');
```

### 2. 定义客户端使用的配制文件及JavaBean
mybatis的核心配制文件,主要定义了数所库的连接及数据库操作的mapper文件 
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<mybatis-config>
    <dataSource>
        <property name="driver-class-name" value="org.sqlite.JDBC"></property>
        <property name="url" value="jdbc:sqlite:mybatis.db"></property>
        <property name="username" value=""></property>
        <property name="password" value=""></property>
    </dataSource>

    <import resource="UserMsgMapper.xml" ></import>
</mybatis-config>
```

UserMsgMapper.xml 此为查询的SQL的影射配制文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<mapper namespace="com.liujun.learn.mybatis.test.dao.UserMsgMapper">
    <select id="findAllUser" type="com.liujun.learn.mybatis.test.bean.UserMsgPO"
            resultType="com.liujun.learn.mybatis.test.bean.UserMsgPO">
        select * from user_msg
    </select>
    <select id="findOne" type="com.liujun.learn.mybatis.test.bean.UserMsgPO"
            resultType="com.liujun.learn.mybatis.test.bean.UserMsgPO">
        select * from user_msg where id = #{id} and name=#{name}
    </select>
    <insert id="insert" type="com.liujun.learn.mybatis.test.bean.UserMsgPO">
        insert into user_msg(id,name)
        values(#{id},#{name})
    </insert>
    <update id="update" type="com.liujun.learn.mybatis.test.bean.UserMsgPO">
        update user_msg
        set name = #{name}
        where id = #{id}
    </update>
    <delete id="delete" type="com.liujun.learn.mybatis.test.bean.UserMsgPO">
        delete from user_msg where id = #{id}
    </delete>
</mapper>
```

数据库中表实体的定义
```java
package com.liujun.learn.mybatis.test.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserMsgPO {

  /** id的信息 */
  private Integer id;

  /** 名称信息 */
  private String name;
}

```


数据库操作的接口定义 
```java
package com.liujun.learn.mybatis.test.dao;

import com.liujun.learn.mybatis.test.bean.UserMsgPO;

import java.util.List;

/**
 * 用户查询的接口定义
 *
 * @author liujun
 * @since 2022/6/29
 */
public interface UserMsgMapper {
    /**
     * 查询所有用户信息
     *
     * @return
     */
    List<UserMsgPO> findAllUser();
    /**
     * 按条件查询单个用户的信息
     *
     * @param userQuery 用户的信息
     * @return 结果集
     */
    UserMsgPO findOne(UserMsgPO userQuery);
    /**
     * 添加数据
     *
     * @param user
     * @return
     */
    int insert(UserMsgPO user);
    /**
     * 修改数据
     *
     * @param user
     * @return
     */
    int update(UserMsgPO user);
    /**
     * 删除数据
     *
     * @param user
     * @return
     */
    int delete(UserMsgPO user);
}
```

## 3 mybatis的框架处理
## 3.1  配制文件解析
要解析配制文件，首先要对配制文件进行定义,使用javaBean对配制文件进行定义
```java
@Getter
@Setter
@ToString
public class MyBatisConfiguration {

  /** 连接池的信息 */
  private DataSource dataSource;

  /** SQL的配制信息 */
  private Map<String, MapperStatement> statementMap;
}
```
对每个数据库操作的Statement进行定义
```java
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
```

mybatis-config的主配制文件解析
```java
/**
 * 配制文件的处理操作
 *
 * @author liujun
 * @since 2022/6/27
 */
public class ConfigParseBuilder {

  private MyBatisConfiguration configuration;

  public ConfigParseBuilder() {
    this.configuration = new MyBatisConfiguration();
  }

  public MyBatisConfiguration readConfig(InputStream input) throws Exception {

    try (BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
      Document dom = new SAXReader().read(bufferedInput);
      Element root = dom.getRootElement();

      // 设置数据源信息
      Element dataSourceElement = root.element("dataSource");
      Properties properties = new Properties();
      List<Element> propertyList = dataSourceElement.elements("property");
      for (Element propertyItem : propertyList) {
        String name = propertyItem.attribute("name").getValue();
        String value = propertyItem.attribute("value").getValue();
        properties.put(name, value);
      }
      configuration.setDataSource(DruidDataSourceFactory.createDataSource(properties));

      Map<String, MapperStatement> mapperElement = new HashMap<>(8);
      List<Element> elementList = root.elements("import");
      for (Element resourceElement : elementList) {
        String resourcePath = resourceElement.attributeValue("resource");
        // 解析配制的SQL信息
        mapperElement.putAll(
            MapperStatementBuilder.readerMapper(ResourceUtils.getResource(resourcePath)));
      }

      configuration.setStatementMap(mapperElement);
    } finally {
      IoUtil.close(input);
    }

    return configuration;
  }
}
```

mapper文件定义
```java
/**
 * 配制文件的处理操作
 *
 * @author liujun
 * @since 2022/6/27
 */
public class MapperStatementBuilder {

  /**
   * 执行配制的文件读取操作
   *
   * @param input
   * @return
   * @throws Exception
   */
  public static Map<String, MapperStatement> readerMapper(InputStream input) throws Exception {

    Map<String, MapperStatement> result;
    Document document = null;
    try (BufferedInputStream buffered = new BufferedInputStream(input)) {
      document = new SAXReader().read(buffered);
      result = new HashMap<>();

      Element rootElement = document.getRootElement();
      String nameSpace = rootElement.attributeValue("namespace");

      // 查询的SQL读取
      List<Element> selectElement = rootElement.elements("select");
      for (Element elementItem : selectElement) {
        String id = elementItem.attribute("id").getValue();
        String type = elementItem.attribute("type").getValue();
        String resultType = elementItem.attribute("resultType").getValue();
        String context = elementItem.getText().trim();

        MapperStatement statement =
            MapperStatement.builder()
                .type(OperatorType.QUERY)
                .id(id)
                .parameterType(type)
                .resultType(resultType)
                .sqlContext(context)
                .build();
        result.put(nameSpace + Symbol.DOC + id, statement);
      }

      // 添加的SQL读取
      List<Element> insertElement = rootElement.elements("insert");
      updateProcess(nameSpace, insertElement, result);

      // 修改SQL读取
      List<Element> updateElement = rootElement.elements("update");
      updateProcess(nameSpace, updateElement, result);

      // 删除的SQL读取
      List<Element> deleteElement = rootElement.elements("delete");
      updateProcess(nameSpace, deleteElement, result);

    } finally {
      IoUtil.close(input);
    }

    return result;
  }

  /**
   * 修改的数据处理
   *
   * @param nameSpace
   * @param listElement
   * @param result
   */
  private static void updateProcess(
      String nameSpace, List<Element> listElement, Map<String, MapperStatement> result) {
    for (Element elementItem : listElement) {
      String id = elementItem.attribute("id").getValue();
      String type = elementItem.attribute("type").getValue();
      String context = elementItem.getText().trim();

      MapperStatement statementUpdate =
          MapperStatement.builder()
              .type(OperatorType.UPDATE)
              .id(id)
              .parameterType(type)
              .sqlContext(context)
              .build();
      result.put(nameSpace + Symbol.DOC + id, statementUpdate);
    }
  }
}
```


## 3.2 创建SQLSessionFactory对象

使用构建器对Factory进行创建
```java
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
```

创建SqlSessionFactory的接口及实现
```java
public interface SqlSessionFactory {

  /**
   * 开始一个SQL的会话处理
   *
   * @return
   */
  SqlSession openSession();
}
```
```java
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
```

再创建SqlSession的接口及实现 
```java
public interface SqlSession {
    /**
     * 执行查询操作
     *
     * @param statementId 指向的SQL的id
     * @param param       运行SQL的参数信息
     * @param <T>         查询的的结果集的类型
     * @return 返回结果集的类型
     */
    <T> List<T> selectList(String statementId, Object... param);
    /**
     * 执行查询操作
     *
     * @param statementId 指向的SQL的id
     * @param param       运行SQL的参数信息
     * @param <T>         查询的的结果集的类型
     * @return 返回结果集的类型
     */
    <T> T selectOne(String statementId, Object... param);
    /**
     * 执行数据库的增册改操作
     *
     * @param statementId 操作的id
     * @param param       参数信息
     * @return 操作影响的数据库行数
     */
    int update(String statementId, Object... param);
    /**
     * 为dao的接口生成代理实现类
     *
     * @param mapperClass 代理的对象类
     * @param <T>         查询的结果集
     */
    <T> T getMapper(Class<?> mapperClass);
}
```

```java
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
```



### 3.3 SQL执行器的接口创建及实现
```java
public interface Executeor {

    /**
     * 通用的查询的接口
     *
     * @param config    配制的信息
     * @param statement 当前所有运行的SQL对象
     * @param param     运行的参数信息
     * @param <E>       查询的结果集对象
     * @return 查询的结果集
     */
    <E> List<E> query(MyBatisConfiguration config, MapperStatement statement, Object... param);

    /**
     * 执行数据库的增删改操作
     *
     * @param config    配制信息
     * @param statement 操作信息
     * @param param     参数信息
     * @return 影响的行数
     */
    int update(MyBatisConfiguration config, MapperStatement statement, Object... param);
}
```

```java
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
```


SQL解析处理
```java
/**
 * SQL的处理对象
 *
 * @author liujun
 * @since 2022/6/27
 */
@Setter
@Getter
@ToString
public class BoundSql {

  /** 开始符号 */
  private static final String START = Symbol.NUMBER_SIGN + Symbol.OPEN_BRACE;

  /** 字符串开始查找对象 */
  private static final CharMatcherBMGoodSuffix MATCH_START = new CharMatcherBMGoodSuffix(START);

  /** 结束符 */
  private static final String END = Symbol.CLOSE_BRACE;

  /** 字符串开始查找对象 */
  private static final CharMatcherBMGoodSuffix MATCH_END = new CharMatcherBMGoodSuffix(END);

  /** SQL对象 */
  private String sql;

  /** SQL的参数对象 */
  private List<BoundParameter> parameterList;

  /**
   * SQL的解析操作
   *
   * @param sql
   * @return
   */
  public static BoundSql sqlParse(String sql) {

    BoundSql sqlInstance = new BoundSql();

    StringBuilder outSql = new StringBuilder();
    byte[] charArrays = CharMatcherBMBadChars.GetBytes(sql);

    // 参数对象
    List<BoundParameter> parameterList = new ArrayList<>();

    int index = -1;
    int startIndex = 0;

    do {
      int leftIndex = MATCH_START.matcherIndex(charArrays, startIndex);
      if (leftIndex == -1) {
        break;
      }
      outSql.append(sql.substring(startIndex, leftIndex));
      outSql.append(Symbol.PROBLEM);
      int rightIndex = MATCH_END.matcherIndex(charArrays, leftIndex);
      String fieldName = sql.substring(leftIndex + START.length(), rightIndex);

      BoundParameter param = new BoundParameter();
      param.setFieldName(fieldName);
      parameterList.add(param);

      startIndex = rightIndex + END.length();

      index++;
    } while (index <= charArrays.length);

    // 如果当前一个参数都未找到，直接将当前SQL作为查询操作的SQL
    if (outSql.length() <= 0) {
      outSql.append(sql);
    }
    // 如果存在SQL，则检查将末尾的SQL进行补齐操作
    else {
      if (startIndex < sql.length()) {
        outSql.append(sql.substring(startIndex));
      }
    }

    sqlInstance.setSql(outSql.toString());
    sqlInstance.setParameterList(parameterList);

    return sqlInstance;
  }
}
```

参数对象
```java
@Getter
@Setter
@ToString
public class BoundParameter {

  /** SQL的属性的名称 */
  private String fieldName;
}
```

至此一个基本的框架已经完成

## 4.客户端调用测试
```java
public class TestUserMsg {

    @Test
    public void query() throws Exception {
        InputStream mybatisConfig =
                this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = factoryBuilder.builder(mybatisConfig);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMsgPO userQuery = new UserMsgPO();
        userQuery.setId(1);
        userQuery.setName("name");

        List<UserMsgPO> list =
                sqlSession.selectList("com.liujun.learn.mybatis.test.dao.UserMsgMapper.findOne", userQuery);
        System.out.println(list);
        Assertions.assertNotNull(list);
    }

    @Test
    public void queryProxy() throws Exception {
        InputStream mybatisConfig =
                this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = factoryBuilder.builder(mybatisConfig);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMsgMapper userMapper = sqlSession.getMapper(UserMsgMapper.class);

        UserMsgPO userQuery = new UserMsgPO();
        userQuery.setId(1);
        userQuery.setName("name");

        UserMsgPO userRsp = userMapper.findOne(userQuery);
        Assertions.assertNotNull(userRsp);

        List<UserMsgPO> userList = userMapper.findAllUser();
        System.out.println(userList);
        Assertions.assertNotNull(userList);
        Assertions.assertNotEquals(0, userList.size());
    }


    @Test
    public void operator() throws Exception {
        InputStream mybatisConfig =
                this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = factoryBuilder.builder(mybatisConfig);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMsgMapper userMapper = sqlSession.getMapper(UserMsgMapper.class);

        UserMsgPO userAdd = new UserMsgPO();
        userAdd.setId(3);
        userAdd.setName("name3");

        //添加用户
        int addUser = userMapper.insert(userAdd);
        Assertions.assertEquals(1, addUser);

        //修改用户
        UserMsgPO userUpdate = new UserMsgPO();
        userUpdate.setId(3);
        userUpdate.setName("name4");
        int updateRsp = userMapper.update(userUpdate);
        Assertions.assertEquals(1, updateRsp);


        //查询用户
        UserMsgPO userQuery = new UserMsgPO();
        userQuery.setId(3);
        userQuery.setName("name4");
        UserMsgPO userRsp = userMapper.findOne(userQuery);
        Assertions.assertNotNull(userRsp);
        Assertions.assertEquals(userRsp.getName(), "name4");


        //删除用户
        UserMsgPO userDelete = new UserMsgPO();
        userDelete.setId(3);
        userDelete.setName("name4");
        int deleteRsp = userMapper.delete(userDelete);
        Assertions.assertEquals(1, updateRsp);
    }
}
```



## 结束
至此一个基本的mybatis的核心框架已经完成，可以对数据库做增册改查操作，并且将查询与代码作为分离。由于还有些代码与此中框架的核心无关，
未放入，如需查看完整的，请参看：[代码仓库](https://github.com/kkzfl22/learn/tree/main/mybatis)



