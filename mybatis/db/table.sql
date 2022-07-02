
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



-- 订单信息
drop table if exists  order_info;

create table order_info(
    id                            	int(8)          not null ,
	money                           int(8)          not null,
	orderTime                       bigint(15)          not null,
	user_id                          int(8)          not null ,
    primary key (id)
) ;


insert into order_info(id,money,orderTime,user_id)
values(10,100,100000000,1);

insert into order_info(id,money,orderTime,user_id)
values(20,200,200000000,2);



