# 自定义mybatis的框架，手写一个


## 1. 加载配制文件
>
> 根据配制文件的路径，加载字节流，存储到内存的resource类中
>


## 2. 创建核心的配制文件Bean
mybatis-config.xml
userMapper.xml
所对应的Bean文件 


## 3. 解析配制文件
使用dom4j进行解析 

## 4. 创建SQLSessionFactory接口及实现DefaultSqlSession


## 5. 创建SqlSession接口及实现DefaultSession.

## 6. 创建Executor接口及实现类，Query(Configuration MapperStatement ,Object ... ) 执行JDBC操作