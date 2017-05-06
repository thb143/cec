-- 新增提前停止退票时间
ALTER TABLE CEC_ChannelSettings
 ADD stopRevokeTime INT NOT NULL AFTER stopSellTime;

-- 默认提前停止退票时间为提前一个小时
UPDATE CEC_ChannelSettings SET stopRevokeTime = 60;
 
-- 新增提前停止退票时间
ALTER TABLE CEC_ChannelShow
 ADD stopRevokeTime DATETIME NOT NULL AFTER stopSellTime;
 
-- 更新之前渠道排期停止退票时间为放映时间前一个小时
UPDATE CEC_ChannelShow SET stopRevokeTime = date_sub(showtime, interval 1 hour);


-- 统计表中新增统计日期
alter table CEC_TicketOrderStat add statDate date not null;
update CEC_TicketOrderStat ct set ct.statDate = (case when ct.kind='1' then date_format(ct.confirmTime,'%Y-%m-%d') when ct.kind='2' then date_format(ct.revokeTime,'%Y-%m-%d') end);

-- 参数设置表中新增日结时间字段
alter table CEC_CircuitSettings add dayStatTime smallint not null default 0;

-- 新建通知用户表
create table CEC_NoticeUser
(
   id                   char(36) not null,
   circuitSettingsId    char(36) not null,
   userId               char(36) not null,
   type                 varchar(3) not null,
   primary key (id)
);

-- 删除原预警用户表
drop table CEC_StatNoticeUser;
drop table CEC_ShowWarnUser;
drop table CEC_OrderWarnUser;

-- 排期异常表增加异常类型字段
alter table CEC_ShowErrorLog add type varchar(3) not null;

-- 之前的排期异常均认为是普通的排期异常
update CEC_ShowErrorLog  t set t.type = '1';

-- 选座票设置表中新增同步排期间隔时间
alter table CEC_TicketSettings add syncShowInterval smallint not null default 30;
update CEC_TicketSettings set syncShowInterval = 30;
-- 选座票设置表中新增最后同步排期时间
alter table CEC_TicketSettings add lastSyncShowTime datetime;