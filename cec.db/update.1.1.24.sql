-- 修改选座票、会员接入类型密码字段长度
alter table CEC_TicketAccessType modify password varchar(60);
alter table CEC_MemberAccessType modify password varchar(60);

-- 修改选座票、会员接入设置密码字段长度
alter table CEC_TicketSettings modify password varchar(60);
alter table CEC_MemberSettings modify password varchar(60);
