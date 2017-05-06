-- 新增排期更新日志表
create table CEC_ShowUpdateLog
(
   id                   char(36) not null,
   showId               char(36) not null,
   creator              varchar(20) not null,
   createDate           datetime not null,
   message              varchar(800) not null,
   origData             varchar(2000) not null,
   newData              varchar(2000) not null,
   primary key (id)
);
alter table CEC_ShowUpdateLog add constraint FK_ShowUpdateLog_showId foreign key (showId)
      references CEC_Show (id) on delete cascade on update restrict;

-- 增加保留排期天数
alter table CEC_TicketSettings add keepShowDays smallint not null default 7;

-- 新增最低价分组、最低价规则表
create table CEC_MinPriceGroup
(
   id                   char(36) not null,
   filmId               char(36) not null,
   name                 varchar(20) not null,
   cityCode             varchar(4000),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

create table CEC_MinPriceRule
(
   id                   char(36) not null,
   groupId              char(36) not null,
   startDate            date not null,
   endDate              date not null,
   priceRule            varchar(800) not null,
   primary key (id)
);

alter table CEC_MinPriceGroup add constraint FK_MinPriceGroup_filmId foreign key (filmId)
      references CEC_Film (id) on delete cascade on update restrict;

alter table CEC_MinPriceRule add constraint FK_MinPriceRule_groupId foreign key (groupId)
      references CEC_MinPriceGroup (id) on delete cascade on update restrict;

-- 移除影片表的备注字段
alter table CEC_Film drop column remark;

-- 移除选座票接入设置表的最低限价字段
alter table CEC_TicketSettings drop column lowestPrice;

-- 参数设置表中新增影片最低价
alter table CEC_CircuitSettings add defaultMinPrice VARCHAR(800) not null default '{"normal2d":15.0,"normal3d":15.0,"max2d":15.0,"max3d":15.0}';

-- 影院表增加接入商字段并处理历史数据
alter table CEC_Cinema add provider varchar(3) not null;
update CEC_Cinema cc set cc.provider = (select cta.provider from CEC_TicketSettings cts,CEC_TicketAccessType cta where 
cc.id =cts.id and cts.accessTypeId = cta.id);

-- 排期表增加接入商字段并处理历史数据
alter table CEC_Show add provider varchar(3) not null;
update CEC_Show csh set csh.provider = (select cc.provider from CEC_Cinema cc where csh.cinemaId = cc.id);

-- 渠道排期表增加接入商字段并处理历史数据
alter table CEC_ChannelShow add provider varchar(3) not null;
update CEC_ChannelShow cch set cch.provider = (select cc.provider from CEC_Cinema cc where cch.cinemaId = cc.id);

-- 新增城市分组
create table CEC_CityGroup
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   cities               varchar(4000),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

-- 增加接口日志长度
alter table CEC_TicketSettings add logLength int not null default 1000;
alter table CEC_MemberSettings add logLength int not null default 1000;
alter table CEC_ChannelSettings add logLength int not null default 1000;

-- 增加参数配置
alter table CEC_TicketSettings add params varchar(120);
alter table CEC_MemberSettings add params varchar(120);

-- 删除最低价规则表
drop table CEC_MinPriceRule;

-- 分组表中增加最低价规则
alter table CEC_MinPriceGroup add minPrices varchar(2000);