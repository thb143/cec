
create table CEC_SnackGroup
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

create table CEC_SnackOrder
(
   id                   char(36) not null,
   channelId            char(36) not null,
   cinemaId             char(36) not null,
   ticketOrderId        char(36),
   code                 varchar(60) not null,
   channelOrderCode     varchar(60),
   mobile               varchar(20),
   createTime           datetime not null,
   revokeTime           datetime,
   amount               decimal(6,2) not null,
   snackCount           smallint not null,
   stdAmount            decimal(6,2) not null,
   cinemaAmount         decimal(6,2) not null,
   channelAmount        decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   channelFee           decimal(6,2) not null,
   status               varchar(3) not null comment '1.未支付 2.已取消 3.已支付 4.出票成功 5.出票失败 6.已退票',
   primary key (id)
);

create unique index IDX_SnackOrder_code on CEC_SnackOrder
(
   code
);

create table CEC_SnackOrderItem
(
   id                   char(36) not null,
   orderId              char(36) not null,
   snackId              char(36) not null,
   salePrice            decimal(6,2) not null,
   stdPrice             decimal(6,2) not null,
   cinemaPrice          decimal(6,2) not null,
   channelPrice         decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   channelFee           decimal(6,2) not null,
   count                int not null,
   snackRuleId			char(36),
   primary key (id)
);

create table CEC_SnackVoucher
(
   id                   char(36) not null,
   code                 varchar(60) not null,
   status               varchar(3) not null comment '0.未验证 1.已验证',
   printable            bool not null,
   reprintCount         int not null,
   genTime              datetime not null,
   confirmPrintTime     datetime,
   expireTime           datetime not null,
   primary key (id)
);

create table CEC_SnackChannel
(
   id                   char(36) not null,
   snackId              char(36) not null,
   channelId            char(36) not null,
   connectFee           decimal(6,2) not null,
   status               varchar(3) not null comment '0.关闭 1.开放',
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

create index IDX_SnackChannel_snackId on CEC_SnackChannel
(
   snackId
);

create index IDX_SnackChannel_channelId on CEC_SnackChannel
(
   channelId
);

create table CEC_BenefitCardConsumeSnackOrder
(
   id                   char(36) not null comment '和SnackOrder一对一关联',
   cardId               char(36) not null,
   discountCount        int not null,
   discountAmount       DECIMAL(6,2) not null,
   primary key (id)
);

DROP TABLE CEC_TicketOrderSnack;

ALTER TABLE CEC_Snack DROP connectFee;

ALTER TABLE CEC_SnackType ADD groupId char(36) not null;
insert into CEC_SnackGroup (id, name, creatorId, createDate, modifierId, modifyDate)
values('ADMINSID-0000-0000-0000-GROUP0000000', '食品', 'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
UPDATE CEC_SnackType SET groupId = 'ADMINSID-0000-0000-0000-GROUP0000000';

ALTER TABLE CEC_TicketOrder DROP snackAmount,DROP snackCinemaAmount,DROP snackChannelAmount,DROP snackConnectFee,DROP snackChannelFee;

alter table CEC_SnackOrderItem add constraint FK_SnackOrderItem_orderId foreign key (orderId)
      references CEC_SnackOrder (id) on delete restrict on update restrict;

alter table CEC_SnackType add constraint FK_SnackType_groupId foreign key (groupId)
      references CEC_SnackGroup (id) on delete restrict on update restrict;

-- 渠道ID记得要换成自有渠道ID
INSERT INTO CEC_SnackChannel SELECT t.id,t.id,'516ccf9b-739d-4616-93b2-b82b3ec44075',0,'1','ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW() FROM CEC_Snack t;

ALTER TABLE CEC_BenefitCard MODIFY COLUMN channelOrderCode VARCHAR(60) NOT NULL;
ALTER TABLE CEC_ChannelSettings MODIFY COLUMN ticketApiMethods VARCHAR(800);
ALTER TABLE CEC_Seat MODIFY COLUMN code varchar(60) not null;



create table CEC_BenefitCardTypeSnackRule
(
   id                   char(36) not null,
   typeId               char(36) not null,
   name                 varchar(60) not null,
   valid                varchar(3) not null comment '0：未生效，1：已生效，2：已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   settleRule           varchar(2000) not null,
   ordinal              int not null,
   boundRuleId          char(36) default NULL,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

create table CEC_BenefitCardTypeSnackRule_Snack
(
   snackRuleId          char(36) not null,
   snackId              char(36) not null
);

alter table CEC_BenefitCardTypeSnackRule add constraint FK_BenefitCardTypeSnackRule_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete cascade on update restrict;