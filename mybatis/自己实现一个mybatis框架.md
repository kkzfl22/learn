## 自已实现一个mybatis框架

>
>使用了mybatis框架这么久了，对原原理也没有深入的了解，正好有此机会，对mybatis的源做做一个深入的分析，在分析之前，
>先自己实现一个简单版本的mybatis框架，然后再阅读源码，这样就能对源码，有更多的深入了解。
>
>开始自己的mybatis框架吧。
>
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

### 2. 定义各户端使用的配制及JavaBean
mybatis的核心配制文件
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

定义的查询操作的