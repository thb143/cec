-- 渠道排期增加关联失效排期编码字段，用于处理生成重复渠道排期问题
alter table CEC_ChannelShow add invalidCode VARCHAR(60) not null;
update CEC_ChannelShow set invalidCode = id;
create unique index IDX_ChannelShow_invalidCode on CEC_ChannelShow
(
   invalidCode
);

-- 排期、渠道排期增加时长，初始值从影片库取。 
alter table CEC_Show add duration SMALLINT not null;
update CEC_Show cs set cs.duration = (select f.duration from CEC_Film f where f.id=cs.filmId);
alter table CEC_ChannelShow add duration SMALLINT not null;
update CEC_ChannelShow cs set cs.duration = (select f.duration from CEC_Film f where f.id=cs.filmId);

-- 设置排期、渠道排期的放映语言允许为空。
alter table CEC_Show modify language varchar(20) null;
alter table CEC_ChannelShow modify language varchar(20) null;