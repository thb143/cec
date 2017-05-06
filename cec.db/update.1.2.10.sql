-- 新增卖品类型表
create table CEC_SnackType
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   image                varchar(800) not null,
   remark               varchar(120),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

-- 新增卖品表
create table CEC_Snack
(
   id                   char(36) not null,
   typeId               char(36) not null,
   cinemaId             char(36) not null,
   code                 varchar(20) not null,
   stdPrice             decimal(6,2) not null,
   submitPrice          decimal(6,2) not null,
   connectFee			decimal(6,2) not null,
   status               varchar(3) not null comment '0:下架 1:上架',
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);
alter table CEC_Snack add constraint FK_Snack_typeId foreign key (typeId)
      references CEC_SnackType (id) on delete cascade on update restrict;
      
create unique index IDX_Snack_code on CEC_Snack
(
   code
);

-- 新增订单卖品明细表
create table CEC_TicketOrderSnack
(
   id                   char(36) not null,
   orderId              char(36) not null,
   snackId              char(36) not null,
   salePrice            decimal(6,2) not null,
   stdPrice             decimal(6,2) not null,
   cinemaPrice          decimal(6,2) not null,
   channelPrice			decimal(6,2) not null,
   connectFee			decimal(6,2) not null,
   channelFee			decimal(6,2) not null,
   count                int not null,
   primary key (id)
);
alter table CEC_TicketOrderSnack add constraint FK_TicketOrderSnack_orderId foreign key (orderId)
      references CEC_TicketOrder (id) on delete cascade on update restrict;

-- 订单表增加卖品相关字段
ALTER TABLE CEC_TicketOrder ADD ticketAmount decimal(6,2) NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackAmount decimal(6,2) NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackCount smallint NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackCinemaAmount decimal(6,2) NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackChannelAmount decimal(6,2) NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackConnectFee decimal(6,2) NOT NULL;
ALTER TABLE CEC_TicketOrder ADD snackChannelFee decimal(6,2) NOT NULL;

update CEC_TicketOrder set ticketAmount=amount;
update CEC_TicketOrder set snackAmount=0;
update CEC_TicketOrder set snackCount=0;
update CEC_TicketOrder set snackCinemaAmount=0;
update CEC_TicketOrder set snackChannelAmount=0;
update CEC_TicketOrder set snackConnectFee=0;
update CEC_TicketOrder set snackChannelFee=0;

-- 选座票明细表中新增二维码字段
alter table CEC_TicketOrderItem add barCode varchar(800);


-- -----------------------------------------------------
-- Table CEC_BenefitCardType
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardType (
  id CHAR(36) NOT NULL,
  code VARCHAR(20) NOT NULL COMMENT '卡类编号',
  name VARCHAR(120) NOT NULL COMMENT '卡类名称',
  prefix VARCHAR(10) NOT NULL COMMENT '卡号规则，前4位',
  initAmount DECIMAL(6,2) NOT NULL COMMENT '开卡金额',
  rechargeAmount DECIMAL(6,2) NOT NULL COMMENT '续费金额',
  validMonth VARCHAR(3) NOT NULL COMMENT '有效时长（月）',
  valid VARCHAR(3) NOT NULL COMMENT '状态 1.启用，0：停用',
  enabled VARCHAR(3) NOT NULL,
  status VARCHAR(3) NOT NULL,
  totalDiscountCount INT NOT NULL,
  dailyDiscountCount INT NOT NULL,
  boundTypeId CHAR(36) DEFAULT NULL,
  creatorId CHAR(36) NOT NULL,
  createDate DATETIME NOT NULL,
  modifierId CHAR(36) NOT NULL,
  modifyDate DATETIME NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table CEC_BenefitCardTypeChannel
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardType_Channel (
  typeId CHAR(36) NOT NULL,
  channelId CHAR(36) NOT NULL
);


-- -----------------------------------------------------
-- Table CEC_BenefitCardTypeRule
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardTypeRule (
  id CHAR(36) NOT NULL,
  typeId CHAR(36) NOT NULL,
  name VARCHAR(45) NOT NULL COMMENT '规则名称',
  valid VARCHAR(3) NOT NULL COMMENT '生效状态',
  enabled VARCHAR(3) NOT NULL COMMENT '启用状态',
  settleRule VARCHAR(2000) NOT NULL COMMENT '结算规则',
  ordinal INT NOT NULL,
  boundRuleId CHAR(36) DEFAULT NULL,
  creatorId CHAR(36) NOT NULL,
  createDate DATETIME NOT NULL,
  modifierId CHAR(36) NOT NULL,
  modifyDate DATETIME NOT NULL,
  PRIMARY KEY (id)
);

alter table CEC_BenefitCardTypeRule add constraint FK_BenefitCardTypeRule_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete cascade on update restrict;


-- -----------------------------------------------------
-- Table CEC_BenefitCardTypeRule_Hall
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardTypeRule_Hall (
  ruleId CHAR(36) NOT NULL,
  hallId CHAR(36) NOT NULL
);


-- -----------------------------------------------------
-- Table CEC_BenefitCardTypeLog
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardTypeLog (
  id CHAR(36) NOT NULL,
  typeId CHAR(36) NOT NULL,
  submitterId CHAR(36) NOT NULL,
  submitTime DATETIME NOT NULL,
  auditorId CHAR(36) NULL,
  auditTime DATETIME NULL,
  approverId CHAR(36) NULL,
  approveTime DATETIME NULL,
  refuseNote VARCHAR(800) NULL,
  status VARCHAR(3) NOT NULL,
  PRIMARY KEY (id)
);

alter table CEC_BenefitCardTypeLog add constraint FK_BenefitCardTypeLog_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete cascade on update restrict;


-- -----------------------------------------------------
-- Table CEC_BenefitCardUser
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardUser (
  id CHAR(36) NOT NULL,
  channelId CHAR(36) NOT NULL,
  mobile VARCHAR(20) NOT NULL,
  createDate DATETIME NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table CEC_BenefitCard
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCard (
  id CHAR(36) NOT NULL,
  typeId CHAR(36) NOT NULL,
  userId CHAR(36) NOT NULL,
  channelId CHAR(36) NOT NULL,
  code VARCHAR(20) NOT NULL,
  channelOrderCode VARCHAR(20) NOT NULL,
  startDate DATETIME NOT NULL,
  endDate DATETIME NOT NULL,
  rechargeCount INT NOT NULL,
  initAmount DECIMAL(6,2) NOT NULL,
  totalDiscountCount INT NOT NULL,
  availableDiscountCount INT NOT NULL,
  discountAmount DECIMAL(6,2) NOT NULL,
  status VARCHAR(3) NOT NULL,
  version INT NOT NULL,
  createDate DATETIME NOT NULL,
  PRIMARY KEY (id)
);

alter table CEC_BenefitCard add constraint FK_BenefitCard_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete cascade on update restrict;

alter table CEC_BenefitCard add constraint FK_BenefitCard_userId foreign key (userId)
      references CEC_BenefitCardUser (id) on delete cascade on update restrict;

create unique index IDX_BenefitCard_channelId_channelOrderCode on CEC_BenefitCard
(
   channelId,channelOrderCode
);

create unique index IDX_BenefitCard_code on CEC_BenefitCard
(
   code
);

create unique index IDX_BenefitCard_typeId_userId_channelId on CEC_BenefitCard
(
   typeId,userId,channelId
);

-- -----------------------------------------------------
-- Table CEC_BenefitCardRechargeOrder
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardRechargeOrder (
  id CHAR(36) NOT NULL,
  cardId CHAR(36) NOT NULL,
  channelId CHAR(36) NOT NULL,
  code VARCHAR(20) NOT NULL,
  channelOrderCode VARCHAR(60) NOT NULL,
  amount DECIMAL(6,2) NOT NULL,
  oldEndDate DATETIME NOT NULL,
  endDate DATETIME DEFAULT NULL,
  totalDiscountCount INT DEFAULT NULL,
  oldDiscountCount INT NOT NULL,
  discountCount INT DEFAULT NULL,
  expireCount INT DEFAULT NULL,
  status VARCHAR(3) NOT NULL,
  createDate DATETIME NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table CEC_BenefitCardConsumeOrder
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardConsumeOrder (
  id CHAR(36) NOT NULL COMMENT '和TicketOrder一对一关联',
  cardId CHAR(36) NOT NULL,
  ruleId CHAR(36) NOT NULL,
  discountCount INT NOT NULL,
  discountAmount DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table CEC_BenefitCardSettle
-- -----------------------------------------------------
CREATE TABLE CEC_BenefitCardSettle (
  id CHAR(36) NOT NULL,
  ruleId CHAR(36) NOT NULL,
  channelShowId CHAR(36) NOT NULL,
  channelPrice DECIMAL(6,2) NOT NULL,
  submitPrice DECIMAL(6,2) NOT NULL,
  cinemaPrice DECIMAL(6,2) NOT NULL,
  circuitFee DECIMAL(6,2) NOT NULL,
  subsidyFee DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (id)
);
