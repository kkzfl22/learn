
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