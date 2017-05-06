/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/8/11 9:07:52                            */
/*==============================================================*/


/*==============================================================*/
/* Table: CEC_Actor                                             */
/*==============================================================*/
create table CEC_Actor
(
   id                   char(36) not null,
   organId              char(36) not null,
   roleId               char(36) not null,
   userId               char(36) not null,
   name                 varchar(60) not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_Attachment                                        */
/*==============================================================*/
create table CEC_Attachment
(
   id                   char(36) not null,
   name                 varchar(60) not null,
   path                 varchar(120) not null,
   createTime           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCard                                       */
/*==============================================================*/
create table CEC_BenefitCard
(
   id                   char(36) not null,
   typeId               char(36) not null,
   userId               char(36) not null,
   channelId            char(36) not null,
   code                 varchar(20) not null,
   channelOrderCode     varchar(60) not null,
   startDate            datetime not null,
   endDate              datetime not null,
   rechargeCount        int not null,
   initAmount           DECIMAL(6,2) not null,
   totalDiscountCount   int not null,
   availableDiscountCount int not null,
   discountAmount       DECIMAL(6,2) not null,
   status               varchar(3) not null comment '1：正常，2：冻结，3：过期',
   firstCinemaId        char(36),
   birthday             datetime,
   version              int not null,
   createDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_BenefitCard_channelId_channelOrderCode            */
/*==============================================================*/
create unique index IDX_BenefitCard_channelId_channelOrderCode on CEC_BenefitCard
(
   channelId,
   channelOrderCode
);

/*==============================================================*/
/* Index: IDX_BenefitCard_code                                  */
/*==============================================================*/
create unique index IDX_BenefitCard_code on CEC_BenefitCard
(
   code
);

/*==============================================================*/
/* Index: IDX_BenefitCard_typeId_userId_channelId               */
/*==============================================================*/
create unique index IDX_BenefitCard_typeId_userId_channelId on CEC_BenefitCard
(
   typeId,
   userId,
   channelId
);

/*==============================================================*/
/* Table: CEC_BenefitCardConsumeOrder                           */
/*==============================================================*/
create table CEC_BenefitCardConsumeOrder
(
   id                   char(36) not null comment '和TicketOrder一对一关联',
   cardId               char(36) not null,
   ruleId               char(36) not null,
   discountCount        int not null,
   discountAmount       DECIMAL(6,2) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardConsumeSnackOrder                      */
/*==============================================================*/
create table CEC_BenefitCardConsumeSnackOrder
(
   id                   char(36) not null comment '和SnackOrder一对一关联',
   cardId               char(36) not null,
   discountCount        int not null,
   discountAmount       DECIMAL(6,2) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardDetailStat                             */
/*==============================================================*/
create table CEC_BenefitCardDetailStat
(
   id                   char(36) not null,
   cardStatId           char(36) not null,
   ticketCount          int not null,
   ticketAmount         decimal(6,2) not null,
   snackCount           int not null,
   snackAmount          decimal(6,2) not null,
   discountAmount       decimal(6,2) not null,
   rechargeCount        int,
   rechargeAmount       decimal(6,2),
   availableDiscountCount int,
   totalDiscountCount   int,
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardRechargeOrder                          */
/*==============================================================*/
create table CEC_BenefitCardRechargeOrder
(
   id                   char(36) not null,
   cardId               char(36) not null,
   channelId            char(36) not null,
   code                 varchar(20) not null,
   channelOrderCode     varchar(60) not null,
   amount               DECIMAL(6,2) not null,
   oldEndDate           datetime not null,
   endDate              datetime default NULL,
   totalDiscountCount   int default NULL,
   oldDiscountCount     int not null,
   discountCount        int default NULL,
   expireCount          int default NULL,
   status               varchar(3) not null comment '1：已支付，2：成功，3：失败',
   createDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardSettle                                 */
/*==============================================================*/
create table CEC_BenefitCardSettle
(
   id                   char(36) not null,
   ruleId               char(36) not null,
   channelShowId        char(36) not null,
   channelPrice         DECIMAL(6,2) not null,
   submitPrice          DECIMAL(6,2) not null,
   cinemaPrice          DECIMAL(6,2) not null,
   circuitFee           DECIMAL(6,2) not null,
   subsidyFee           DECIMAL(6,2) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardStat                                   */
/*==============================================================*/
create table CEC_BenefitCardStat
(
   id                   char(36) not null,
   cardId               char(36) not null,
   cardTypeId           char(36) not null,
   channelId            char(36) not null,
   largess              varchar(20) not null,
   discountAmount       varchar(60) not null,
   initAmount           decimal(6,2) not null,
   rechargeAmount       decimal(6,2) not null,
   largessAmount        decimal(6,2) not null comment '1. 普通 2.特价',
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardType                                   */
/*==============================================================*/
create table CEC_BenefitCardType
(
   id                   char(36) not null,
   code                 varchar(20) not null,
   name                 varchar(120) not null,
   prefix               varchar(10) not null comment '卡号前4位',
   initAmount           DECIMAL(6,2) not null,
   rechargeAmount       DECIMAL(6,2) not null,
   validMonth           varchar(3) not null comment '单位：月',
   valid                varchar(3) not null comment '0：未生效，1：已生效，2：已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '1.待提交 2.待审核 3.待审批 4.已审批 5.已退回',
   totalDiscountCount   int not null,
   dailyDiscountCount   int not null,
   boundTypeId          char(36) default NULL,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardTypeLog                                */
/*==============================================================*/
create table CEC_BenefitCardTypeLog
(
   id                   char(36) not null,
   typeId               char(36) not null,
   submitterId          char(36) not null,
   submitTime           datetime not null,
   auditorId            char(36),
   auditTime            datetime,
   approverId           char(36),
   approveTime          datetime,
   refuseNote           varchar(800),
   status               varchar(3) not null comment '1.待审核 2.待审批 3.已审批 4.已退回 5.已处理',
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardTypeRule                               */
/*==============================================================*/
create table CEC_BenefitCardTypeRule
(
   id                   char(36) not null,
   typeId               char(36) not null,
   name                 varchar(60) not null,
   valid                varchar(3) not null comment '0：未生效，1：已生效，2：已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   settleRule           varchar(2000) not null,
   showTypes            varchar(60) not null,
   ordinal              int not null,
   boundRuleId          char(36) default NULL,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BenefitCardTypeRule_Hall                          */
/*==============================================================*/
create table CEC_BenefitCardTypeRule_Hall
(
   ruleId               char(36) not null,
   hallId               char(36) not null
);

/*==============================================================*/
/* Table: CEC_BenefitCardTypeSnackRule                          */
/*==============================================================*/
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

/*==============================================================*/
/* Table: CEC_BenefitCardTypeSnackRule_Snack                    */
/*==============================================================*/
create table CEC_BenefitCardTypeSnackRule_Snack
(
   snackRuleId          char(36) not null,
   snackId              char(36) not null
);

/*==============================================================*/
/* Table: CEC_BenefitCardType_Channel                           */
/*==============================================================*/
create table CEC_BenefitCardType_Channel
(
   typeId               char(36) not null,
   channelId            char(36) not null
);

/*==============================================================*/
/* Table: CEC_BenefitCardUser                                   */
/*==============================================================*/
create table CEC_BenefitCardUser
(
   id                   char(36) not null,
   channelId            char(36) not null,
   mobile               varchar(20) not null,
   createDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_BnLog                                             */
/*==============================================================*/
create table CEC_BnLog
(
   id                   char(36) not null,
   creator              varchar(20) not null,
   createDate           datetime not null,
   message              varchar(800) not null,
   entityId             char(36),
   origData             varchar(2000),
   newData              varchar(2000),
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_BnLog_entityId                                    */
/*==============================================================*/
create index IDX_BnLog_entityId on CEC_BnLog
(
   entityId
);

/*==============================================================*/
/* Table: CEC_Channel                                           */
/*==============================================================*/
create table CEC_Channel
(
   id                   char(36) not null,
   name                 varchar(60) not null,
   code                 varchar(60) not null,
   secKey               varchar(60) not null,
   type                 varchar(3) not null comment '0、电商渠道，1、接口渠道',
   salable              bool not null comment '0.关闭销售 1.开放销售',
   opened               bool not null comment '0.关闭 1.开放',
   remark               varchar(800),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ChannelPolicy                                     */
/*==============================================================*/
create table CEC_ChannelPolicy
(
   id                   char(36) not null,
   channelId            char(36) not null,
   name                 varchar(120) not null,
   startDate            date not null,
   endDate              date not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '1.待提交 2.待审核 3.待审批 4.已审批 5.已退回',
   ordinal              int not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_ChannelPolicy_channelId                           */
/*==============================================================*/
create index IDX_ChannelPolicy_channelId on CEC_ChannelPolicy
(
   channelId
);

/*==============================================================*/
/* Table: CEC_ChannelPolicyLog                                  */
/*==============================================================*/
create table CEC_ChannelPolicyLog
(
   id                   char(36) not null,
   policyId             char(36) not null comment '关联策略ID',
   submitterId          char(36) not null,
   submitTime           datetime not null,
   auditorId            char(36),
   auditTime            datetime,
   approverId           char(36),
   approveTime          datetime,
   status               varchar(3) not null comment '1.待审核 2.待审批 3.已审批 4.已退回 5.已处理',
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ChannelRule                                       */
/*==============================================================*/
create table CEC_ChannelRule
(
   id                   char(36) not null,
   groupId              char(36) not null comment '关联分组ID',
   name                 varchar(120) not null,
   showType             varchar(3) not null,
   periodRule           varchar(2000) not null,
   settleRule           varchar(2000) not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效 2.已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '-1.未审核 0.不通过 1.通过',
   boundRuleId          char(36),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ChannelRuleGroup                                  */
/*==============================================================*/
create table CEC_ChannelRuleGroup
(
   id                   char(36) not null,
   policyId             char(36) not null,
   cinemaId             char(36) not null,
   ordinal              int not null,
   status               varchar(3) not null comment '0.关闭 1.开放',
   connectFee           decimal(6,2) not null,
   valid                tinyint not null default 1 comment '当分组下没有未生效的规则时，为true，否则为false，用于渠道结算策略页面展示的优化',
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_ChannelRuleGroup_cinemaId                         */
/*==============================================================*/
create index IDX_ChannelRuleGroup_cinemaId on CEC_ChannelRuleGroup
(
   cinemaId
);

/*==============================================================*/
/* Table: CEC_ChannelRule_Hall                                  */
/*==============================================================*/
create table CEC_ChannelRule_Hall
(
   ruleId               char(36) not null,
   hallId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_ChannelRule_Hall_ruleId                           */
/*==============================================================*/
create index IDX_ChannelRule_Hall_ruleId on CEC_ChannelRule_Hall
(
   ruleId
);

/*==============================================================*/
/* Index: IDX_ChannelRule_Hall_hallId                           */
/*==============================================================*/
create index IDX_ChannelRule_Hall_hallId on CEC_ChannelRule_Hall
(
   hallId
);

/*==============================================================*/
/* Table: CEC_ChannelSettings                                   */
/*==============================================================*/
create table CEC_ChannelSettings
(
   id                   char(36) not null,
   stopSellTime         int not null,
   stopRevokeTime       int not null,
   logLength            int not null,
   verifySign           varchar(3) not null comment '0、不验证，1 、验证',
   ticketApiMethods     varchar(800),
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ChannelShow                                       */
/*==============================================================*/
create table CEC_ChannelShow
(
   id                   char(36) not null,
   showId               char(36) not null,
   channelId            char(36) not null,
   cinemaId             char(36) not null,
   hallId               char(36) not null,
   filmId               char(36) not null,
   provider             varchar(3) not null comment '001：国标 002：鼎新 003：火凤凰 004：满天星 005：测试',
   code                 varchar(20) not null,
   type                 varchar(3) not null comment '1.普通 2.特价',
   showCode             varchar(60) not null,
   filmCode             varchar(20) not null,
   language             varchar(20),
   showType             varchar(3) not null,
   showTime             datetime not null comment '放映时间',
   duration             smallint not null,
   expireTime           datetime not null,
   minPrice             decimal(6,2) not null,
   stdPrice             decimal(6,2) not null,
   cinemaRuleId         char(36),
   channelRuleId        char(36),
   specialRuleId        char(36),
   specialChannelId     char(36),
   cinemaPrice          decimal(6,2) not null,
   channelPrice         decimal(6,2) not null,
   submitPrice          decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   circuitFee           decimal(6,2) not null,
   subsidyFee           decimal(6,2) not null,
   stopSellTime         datetime not null,
   stopRevokeTime       datetime not null,
   status               varchar(3) not null comment '0.下架 1.上架 2.失效',
   invalidCode          varchar(60) not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_ChannelShow_code                                  */
/*==============================================================*/
create unique index IDX_ChannelShow_code on CEC_ChannelShow
(
   code
);

/*==============================================================*/
/* Index: IDX_ChannelShow_invalidCode                           */
/*==============================================================*/
create unique index IDX_ChannelShow_invalidCode on CEC_ChannelShow
(
   invalidCode
);

/*==============================================================*/
/* Table: CEC_ChannelTicketOrderDaily                           */
/*==============================================================*/
create table CEC_ChannelTicketOrderDaily
(
   id                   char(36) not null,
   channelId            char(36) not null,
   showTypeStat         varchar(2000) not null,
   normalOrderStat      varchar(800) not null,
   refundOrderStat      varchar(800) not null,
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_ChannelTicketOrderDaily_channelId                 */
/*==============================================================*/
create index IDX_ChannelTicketOrderDaily_channelId on CEC_ChannelTicketOrderDaily
(
   channelId
);

/*==============================================================*/
/* Table: CEC_Cinema                                            */
/*==============================================================*/
create table CEC_Cinema
(
   id                   char(36) not null,
   code                 varchar(20) not null,
   name                 varchar(120) not null,
   provider             varchar(3) not null,
   hallCount            smallint not null comment '影厅数量',
   county               varchar(20) not null,
   address              varchar(120) not null,
   logo                 varchar(120) comment '影院LOGO',
   url                  varchar(120) comment ' 网址',
   tel                  varchar(120) not null comment '客服电话',
   devicePos            varchar(120),
   deviceImg            varchar(120),
   grade                decimal(8,2),
   ordinal              int not null,
   intro                varchar(4000),
   busLine              varchar(800) comment '注：用于淘宝接口',
   longitude            varchar(20),
   latitude             varchar(20),
   feature              varchar(2000),
   status               varchar(3) not null comment '0. 停用 1. 启用',
   salable              bool not null comment '0.关闭销售 1.开放销售',
   ticketSetted         bool not null comment '0.未设置 1.已设置',
   memberSetted         bool not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_CinemaChannelTicketOrderDaily                     */
/*==============================================================*/
create table CEC_CinemaChannelTicketOrderDaily
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   channelId            char(36) not null,
   showTypeStat         varchar(2000) not null,
   normalOrderStat      varchar(800) not null,
   refundOrderStat      varchar(800) not null,
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_CinemaChannelTicketOrderDaily_cinemaId            */
/*==============================================================*/
create index IDX_CinemaChannelTicketOrderDaily_cinemaId on CEC_CinemaChannelTicketOrderDaily
(
   cinemaId
);

/*==============================================================*/
/* Index: IDX_CinemaChannelTicketOrderDaily_channelId           */
/*==============================================================*/
create index IDX_CinemaChannelTicketOrderDaily_channelId on CEC_CinemaChannelTicketOrderDaily
(
   channelId
);

/*==============================================================*/
/* Table: CEC_CinemaPolicy                                      */
/*==============================================================*/
create table CEC_CinemaPolicy
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   name                 varchar(120) not null,
   startDate            date not null,
   endDate              date not null,
   submitType           varchar(3) not null,
   amount               decimal(6,2) not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '1.待提交 2.待审核 3.待审批 4.已审批 5.已退回',
   ordinal              int not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_CinemaPolicy_cinemaId                             */
/*==============================================================*/
create index IDX_CinemaPolicy_cinemaId on CEC_CinemaPolicy
(
   cinemaId
);

/*==============================================================*/
/* Table: CEC_CinemaPolicyLog                                   */
/*==============================================================*/
create table CEC_CinemaPolicyLog
(
   id                   char(36) not null,
   policyId             char(36) not null comment '关联策略ID',
   submitterId          char(36) not null,
   submitTime           datetime not null,
   auditorId            char(36),
   auditTime            datetime,
   approverId           char(36),
   approveTime          datetime,
   status               varchar(3) not null comment '1.待审核 2.待审批 3.已审批 4.已退回 5.已处理',
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_CinemaRule                                        */
/*==============================================================*/
create table CEC_CinemaRule
(
   id                   char(36) not null,
   policyId             char(36) not null comment '关联策略ID',
   name                 varchar(120) not null,
   showType             varchar(3) not null,
   periodRule           varchar(2000) not null,
   settleRule           varchar(2000) not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效 2.已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '-1.未审核 0.不通过 1.通过',
   boundRuleId          char(36),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_CinemaRule_Hall                                   */
/*==============================================================*/
create table CEC_CinemaRule_Hall
(
   ruleId               char(36) not null,
   hallId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_CinemaRule_Hall_ruleId                            */
/*==============================================================*/
create index IDX_CinemaRule_Hall_ruleId on CEC_CinemaRule_Hall
(
   ruleId
);

/*==============================================================*/
/* Index: IDX_CinemaRule_Hall_hallId                            */
/*==============================================================*/
create index IDX_CinemaRule_Hall_hallId on CEC_CinemaRule_Hall
(
   hallId
);

/*==============================================================*/
/* Table: CEC_CinemaTicketOrderDaily                            */
/*==============================================================*/
create table CEC_CinemaTicketOrderDaily
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   showTypeStat         varchar(2000) not null,
   normalOrderStat      varchar(800) not null,
   refundOrderStat      varchar(800) not null,
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_CinemaTicketOrderDaily_cinemaId                   */
/*==============================================================*/
create index IDX_CinemaTicketOrderDaily_cinemaId on CEC_CinemaTicketOrderDaily
(
   cinemaId
);

/*==============================================================*/
/* Table: CEC_CircuitSettings                                   */
/*==============================================================*/
create table CEC_CircuitSettings
(
   id                   char(36) not null,
   settleRuleTypes      varchar(20) not null comment '院线设定需要的结算规则类型。',
   lockSeatTime         int not null,
   defaultMinPrice      varchar(800) not null,
   dayStatTime          smallint not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_CityGroup                                         */
/*==============================================================*/
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

/*==============================================================*/
/* Table: CEC_Film                                              */
/*==============================================================*/
create table CEC_Film
(
   id                   char(36) not null,
   code                 varchar(60) not null,
   name                 varchar(120) not null,
   duration             smallint not null,
   publishDate          date,
   publisher            varchar(800),
   director             varchar(120) not null,
   cast                 varchar(800),
   intro                varchar(2000),
   showTypes            varchar(60),
   country              varchar(120) not null,
   language             varchar(60) not null,
   type                 varchar(60) not null comment '科幻@惊悚@爱情',
   poster               varchar(2000) comment '注：用于淘宝接口',
   stills               text comment '注：用于淘宝接口',
   trailers             varchar(800),
   highlight            varchar(2000) comment '注：用于淘宝接口',
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_Film_code                                         */
/*==============================================================*/
create unique index IDX_Film_code on CEC_Film
(
   code
);

/*==============================================================*/
/* Table: CEC_FilmErrorLog                                      */
/*==============================================================*/
create table CEC_FilmErrorLog
(
   id                   char(36) not null,
   syncLogId            char(36) not null,
   code                 varchar(60) not null,
   name                 varchar(120) not null,
   msg                  varchar(800) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_FilmSyncLog                                       */
/*==============================================================*/
create table CEC_FilmSyncLog
(
   id                   char(36) not null,
   syncTime             datetime not null,
   duration             int not null,
   processCount         int not null,
   createCount          int not null,
   updateCount          int not null,
   errorCount           int not null,
   status               varchar(3) not null,
   msg                  text,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_Hall                                              */
/*==============================================================*/
create table CEC_Hall
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   code                 varchar(20) not null,
   name                 varchar(60) not null,
   type                 char(36),
   seatCount            smallint not null,
   status               varchar(3) not null comment '0.停用 1.启用 2.删除',
   remark               varchar(800),
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_Hall_code                                         */
/*==============================================================*/
create unique index IDX_Hall_code on CEC_Hall
(
   code,
   cinemaId
);

/*==============================================================*/
/* Table: CEC_HallType                                          */
/*==============================================================*/
create table CEC_HallType
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_MemberAccessType                                  */
/*==============================================================*/
create table CEC_MemberAccessType
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   provider             varchar(3) not null,
   adapter              varchar(3) not null,
   model                varchar(3) not null comment '1.中心接入 2.单家接入',
   url                  varchar(800),
   username             varchar(20),
   password             varchar(60),
   params               varchar(120),
   connectTimeout       smallint not null,
   socketTimeout        smallint not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_MemberSettings                                    */
/*==============================================================*/
create table CEC_MemberSettings
(
   id                   char(36) not null,
   accessTypeId         char(36) not null,
   logLength            int not null,
   url                  varchar(800),
   username             varchar(20),
   password             varchar(60),
   params               varchar(120),
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_MinPriceGroup                                     */
/*==============================================================*/
create table CEC_MinPriceGroup
(
   id                   char(36) not null,
   filmId               char(36) not null,
   name                 varchar(20) not null,
   cityCode             varchar(4000),
   minPrices            varchar(2000),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_NoticeUser                                        */
/*==============================================================*/
create table CEC_NoticeUser
(
   id                   char(36) not null,
   circuitSettingsId    char(36) not null,
   userId               char(36) not null,
   type                 varchar(3) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_Organ                                             */
/*==============================================================*/
create table CEC_Organ
(
   id                   char(36) not null,
   parentId             char(36),
   name                 varchar(60) not null,
   ordinal              int,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_Role                                              */
/*==============================================================*/
create table CEC_Role
(
   id                   char(36) not null,
   name                 varchar(60) not null,
   permissions          varchar(800) not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_Seat                                              */
/*==============================================================*/
create table CEC_Seat
(
   id                   char(36) not null,
   hallId               char(36) not null,
   code                 varchar(60) not null,
   groupCode            varchar(20) not null comment '注：一个影厅的分区，如：上下层',
   rowNum               varchar(20) not null,
   colNum               varchar(20) not null,
   xCoord               int not null,
   yCoord               int not null,
   type                 varchar(3) not null comment '1.普通 2.情侣',
   status               varchar(3) not null comment '0.不可用 1.可用',
   loveCode             varchar(60) comment '情侣座编码',
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_Seat_code                                         */
/*==============================================================*/
create unique index IDX_Seat_code on CEC_Seat
(
   code,
   hallId
);

/*==============================================================*/
/* Table: CEC_Show                                              */
/*==============================================================*/
create table CEC_Show
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   hallId               char(36) not null,
   filmId               char(36) not null,
   provider             varchar(3) not null comment '001：国标 002：鼎新 003：火凤凰 004：满天星 005：测试',
   code                 varchar(60) not null,
   filmCode             varchar(20) not null,
   language             varchar(20),
   showType             varchar(3) not null,
   showTime             datetime not null comment '放映时间',
   duration             smallint not null,
   expireTime           datetime not null,
   through              bool not null comment '0.非连场 1.连场',
   minPrice             decimal(6,2) not null,
   stdPrice             decimal(6,2) not null,
   status               varchar(3) not null comment '1.新建 2.更新 3.删除 4.过期',
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_Show_code                                         */
/*==============================================================*/
create unique index IDX_Show_code on CEC_Show
(
   cinemaId,
   code
);

/*==============================================================*/
/* Table: CEC_ShowErrorLog                                      */
/*==============================================================*/
create table CEC_ShowErrorLog
(
   id                   char(36) not null,
   syncLogId            char(36) not null,
   showCode             varchar(60) not null,
   cinemaCode           varchar(20) not null,
   hallCode             varchar(20) not null,
   filmCode             varchar(20) not null,
   showTime             datetime not null,
   through              bool not null,
   minPrice             double(6,2) not null,
   stdPrice             double(6,2) not null,
   type                 varchar(3) not null,
   msg                  varchar(800) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ShowSyncLog                                       */
/*==============================================================*/
create table CEC_ShowSyncLog
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   syncTime             datetime not null,
   duration             int not null,
   processCount         int not null,
   createCount          int not null,
   updateCount          int not null,
   deleteCount          int not null,
   errorCount           int not null,
   status               varchar(3) not null comment '1.同步成功 2.同步失败 3.数据异常',
   msg                  text,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_ShowUpdateLog                                     */
/*==============================================================*/
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

/*==============================================================*/
/* Table: CEC_Snack                                             */
/*==============================================================*/
create table CEC_Snack
(
   id                   char(36) not null,
   typeId               char(36) not null,
   cinemaId             char(36) not null,
   code                 varchar(20) not null,
   stdPrice             decimal(6,2) not null,
   submitPrice          decimal(6,2) not null,
   status               varchar(3) not null comment '0:下架 1:上架',
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_Snack_code                                        */
/*==============================================================*/
create unique index IDX_Snack_code on CEC_Snack
(
   code
);

/*==============================================================*/
/* Table: CEC_SnackChannel                                      */
/*==============================================================*/
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

/*==============================================================*/
/* Index: IDX_SnackChannel_snackId                              */
/*==============================================================*/
create index IDX_SnackChannel_snackId on CEC_SnackChannel
(
   snackId
);

/*==============================================================*/
/* Index: IDX_SnackChannel_channelId                            */
/*==============================================================*/
create index IDX_SnackChannel_channelId on CEC_SnackChannel
(
   channelId
);

/*==============================================================*/
/* Table: CEC_SnackGroup                                        */
/*==============================================================*/
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

/*==============================================================*/
/* Table: CEC_SnackOrder                                        */
/*==============================================================*/
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
   discountAmount       decimal(6,2) not null,
   status               varchar(3) not null comment '1.未支付 2.已取消 3.已支付 4.出票成功 5.出票失败 6.已退票',
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_SnackOrder_code                                   */
/*==============================================================*/
create unique index IDX_SnackOrder_code on CEC_SnackOrder
(
   code
);

/*==============================================================*/
/* Table: CEC_SnackOrderItem                                    */
/*==============================================================*/
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
   discountPrice        decimal(6,2) not null,
   count                int not null,
   snackRuleId          char(36),
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_SnackType                                         */
/*==============================================================*/
create table CEC_SnackType
(
   id                   char(36) not null,
   groupId              char(36) not null,
   name                 varchar(20) not null,
   image                varchar(800) not null,
   remark               varchar(120),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_SnackVoucher                                      */
/*==============================================================*/
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

/*==============================================================*/
/* Table: CEC_SpecialChannel                                    */
/*==============================================================*/
create table CEC_SpecialChannel
(
   id                   char(36) not null,
   ruleId               char(36) not null,
   channelId            char(36) not null,
   settleRule           varchar(2000) not null,
   connectFee           decimal(6,2) not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效 2.已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   boundChannelId       char(36),
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_SpecialChannel_channelId                          */
/*==============================================================*/
create index IDX_SpecialChannel_channelId on CEC_SpecialChannel
(
   channelId
);

/*==============================================================*/
/* Table: CEC_SpecialPolicy                                     */
/*==============================================================*/
create table CEC_SpecialPolicy
(
   id                   char(36) not null,
   name                 varchar(60) not null,
   type                 varchar(3) not null comment '1.促销 2.特殊影片',
   showTypes            varchar(60) not null,
   startDate            date not null,
   endDate              date not null,
   showStartDate        date not null,
   showEndDate          date not null,
   exclusive            bool not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效 2.已失效。',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   status               varchar(3) not null comment '1.待提交 2.待审核 3.待审批 4.已审批 5.已退回',
   ordinal              int not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_SpecialPolicyLog                                  */
/*==============================================================*/
create table CEC_SpecialPolicyLog
(
   id                   char(36) not null,
   policyId             char(36) not null,
   submitterId          char(36) not null,
   submitTime           datetime not null,
   submitRemark         varchar(800) not null,
   auditorId            char(36),
   auditTime            datetime,
   approverId           char(36),
   approveTime          datetime,
   refuseNote           varchar(800),
   status               varchar(3) not null comment '1.待审核 2.待审批 3.已审批 4.已退回 5.已处理',
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_SpecialPolicy_Channel                             */
/*==============================================================*/
create table CEC_SpecialPolicy_Channel
(
   policyId             char(36) not null,
   channelId            char(36) not null
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Channel_policyId                    */
/*==============================================================*/
create index IDX_SpecialPolicy_Channel_policyId on CEC_SpecialPolicy_Channel
(
   policyId
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Channel_channelId                   */
/*==============================================================*/
create index IDX_SpecialPolicy_Channel_channelId on CEC_SpecialPolicy_Channel
(
   channelId
);

/*==============================================================*/
/* Table: CEC_SpecialPolicy_Film                                */
/*==============================================================*/
create table CEC_SpecialPolicy_Film
(
   policyId             char(36) not null,
   filmId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Film_policyId                       */
/*==============================================================*/
create index IDX_SpecialPolicy_Film_policyId on CEC_SpecialPolicy_Film
(
   policyId
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Film_filmId                         */
/*==============================================================*/
create index IDX_SpecialPolicy_Film_filmId on CEC_SpecialPolicy_Film
(
   filmId
);

/*==============================================================*/
/* Table: CEC_SpecialPolicy_Hall                                */
/*==============================================================*/
create table CEC_SpecialPolicy_Hall
(
   policyId             char(36) not null,
   hallId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Hall_policyId                       */
/*==============================================================*/
create index IDX_SpecialPolicy_Hall_policyId on CEC_SpecialPolicy_Hall
(
   policyId
);

/*==============================================================*/
/* Index: IDX_SpecialPolicy_Hall_hallId                         */
/*==============================================================*/
create index IDX_SpecialPolicy_Hall_hallId on CEC_SpecialPolicy_Hall
(
   hallId
);

/*==============================================================*/
/* Table: CEC_SpecialRule                                       */
/*==============================================================*/
create table CEC_SpecialRule
(
   id                   char(36) not null,
   policyId             char(36) not null comment '关联策略ID',
   name                 varchar(60) not null,
   showType             varchar(3) not null,
   periodRule           varchar(2000) not null,
   settleRule           varchar(2000) not null,
   submitType           varchar(3) not null,
   amount               decimal(6,2) not null,
   valid                varchar(3) not null comment '0.未生效 1.已生效 2.已失效',
   enabled              varchar(3) not null comment '0.停用 1.启用',
   boundRuleId          char(36),
   ordinal              int not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_SpecialRule_Film                                  */
/*==============================================================*/
create table CEC_SpecialRule_Film
(
   ruleId               char(36) not null,
   filmId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_SpecialRule_Film_ruleId                           */
/*==============================================================*/
create index IDX_SpecialRule_Film_ruleId on CEC_SpecialRule_Film
(
   ruleId
);

/*==============================================================*/
/* Index: IDX_SpecialRule_Film_filmId                           */
/*==============================================================*/
create index IDX_SpecialRule_Film_filmId on CEC_SpecialRule_Film
(
   filmId
);

/*==============================================================*/
/* Table: CEC_SpecialRule_Hall                                  */
/*==============================================================*/
create table CEC_SpecialRule_Hall
(
   ruleId               char(36) not null,
   hallId               char(36) not null
);

/*==============================================================*/
/* Index: IDX_SpecialRule_Hall_ruleId                           */
/*==============================================================*/
create index IDX_SpecialRule_Hall_ruleId on CEC_SpecialRule_Hall
(
   ruleId
);

/*==============================================================*/
/* Index: IDX_SpecialRule_Hall_hallId                           */
/*==============================================================*/
create index IDX_SpecialRule_Hall_hallId on CEC_SpecialRule_Hall
(
   hallId
);

/*==============================================================*/
/* Table: CEC_TicketAccessType                                  */
/*==============================================================*/
create table CEC_TicketAccessType
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   provider             varchar(3) not null,
   adapter              varchar(3) not null,
   model                varchar(3) not null comment '1.中心接入 2.单家接入',
   url                  varchar(800),
   username             varchar(20),
   password             varchar(60),
   params               varchar(120),
   connectTimeout       smallint not null,
   socketTimeout        smallint not null,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_TicketOrder                                       */
/*==============================================================*/
create table CEC_TicketOrder
(
   id                   char(36) not null,
   channelId            char(36) not null,
   cinemaId             char(36) not null,
   hallId               char(36) not null,
   filmId               char(36) not null,
   provider             varchar(3) not null comment '001：国标 002：鼎新 003：火凤凰 004：满天星 005：测试',
   showCode             varchar(60) not null,
   channelShowCode      varchar(20) not null,
   filmCode             varchar(20) not null,
   language             varchar(20),
   showType             varchar(3) not null,
   showTime             datetime not null comment '放映时间',
   duration             smallint not null,
   stopSellTime         datetime not null,
   stopRevokeTime       datetime not null,
   minPrice             decimal(6,2) not null,
   stdPrice             decimal(6,2) not null,
   cinemaRuleId         char(36),
   channelRuleId        char(36),
   specialRuleId        char(36),
   specialChannelId     char(36),
   singleCinemaPrice    decimal(6,2) not null,
   singleChannelPrice   decimal(6,2) not null,
   singleSubmitPrice    decimal(6,2) not null,
   singleConnectFee     decimal(6,2) not null,
   singleCircuitFee     decimal(6,2) not null,
   singleSubsidyFee     decimal(6,2) not null,
   channelShowCreateDate datetime not null,
   channelShowModifyDate datetime not null,
   type                 varchar(3) not null comment '1. 普通 2.特价',
   code                 varchar(60) not null,
   cinemaOrderCode      varchar(60) not null,
   channelOrderCode     varchar(60),
   mobile               varchar(20),
   createTime           datetime not null,
   expireTime           datetime not null,
   payTime              datetime,
   confirmTime          datetime,
   revokeTime           datetime,
   amount               decimal(6,2) not null,
   ticketAmount         decimal(6,2) not null,
   ticketCount          smallint not null,
   snackCount           smallint not null,
   cinemaAmount         decimal(6,2) not null,
   channelAmount        decimal(6,2) not null,
   submitAmount         decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   circuitFee           decimal(6,2) not null,
   subsidyFee           decimal(6,2) not null,
   channelFee           decimal(6,2) not null,
   serviceFee           decimal(6,2) not null,
   discountAmount       decimal(6,2) not null,
   status               varchar(3) not null comment '1.未支付 2.已取消 3.已支付 4.出票成功 5.出票失败 6.已退票',
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_TicketOrder_code                                  */
/*==============================================================*/
create unique index IDX_TicketOrder_code on CEC_TicketOrder
(
   code
);

/*==============================================================*/
/* Table: CEC_TicketOrderItem                                   */
/*==============================================================*/
create table CEC_TicketOrderItem
(
   id                   char(36) not null,
   orderId              char(36) not null,
   ticketCode           varchar(20),
   seatCode             varchar(20) not null,
   barCode              varchar(800),
   seatGroupCode        varchar(20) not null,
   seatRow              varchar(20) not null,
   seatCol              varchar(20) not null,
   salePrice            decimal(6,2) not null,
   cinemaPrice          decimal(6,2) not null,
   channelPrice         decimal(6,2) not null,
   circuitFee           decimal(6,2) not null,
   subsidyFee           decimal(6,2) not null,
   channelFee           decimal(6,2) not null,
   serviceFee           decimal(6,2) not null,
   submitPrice          decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   discountPrice        decimal(6,2) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_TicketOrderStat                                   */
/*==============================================================*/
create table CEC_TicketOrderStat
(
   id                   char(36) not null,
   cinemaId             char(36) not null,
   hallId               char(36) not null,
   channelId            char(36) not null,
   filmId               char(36) not null,
   specialPolicyId      char(36),
   provider             varchar(3) not null comment '001：国标 002：鼎新 003：火凤凰 004：满天星 005：测试',
   code                 varchar(60) not null,
   cinemaOrderCode      varchar(60) not null,
   channelOrderCode     varchar(60) not null,
   type                 varchar(3) not null comment '1. 普通 2.特价',
   kind                 varchar(3) not null comment '1. 正常 2.退票',
   showType             varchar(3) not null comment '1.2D  2,3D  3.MAX2D 4.MAX3D',
   mobile               varchar(20) not null,
   confirmTime          datetime not null,
   revokeTime           datetime,
   ticketCount          smallint not null,
   amount               decimal(6,2) not null,
   cinemaAmount         decimal(6,2) not null,
   channelAmount        decimal(6,2) not null,
   submitAmount         decimal(6,2) not null,
   connectFee           decimal(6,2) not null,
   circuitFee           decimal(6,2) not null,
   subsidyFee           decimal(6,2) not null,
   channelFee           decimal(6,2) not null,
   serviceFee           decimal(6,2) not null,
   statDate             date not null,
   primary key (id)
);

/*==============================================================*/
/* Index: IDX_TicketNormalOrder_cinemaId                        */
/*==============================================================*/
create index IDX_TicketNormalOrder_cinemaId on CEC_TicketOrderStat
(
   cinemaId
);

/*==============================================================*/
/* Index: IDX_TicketNormalOrder_channelId                       */
/*==============================================================*/
create index IDX_TicketNormalOrder_channelId on CEC_TicketOrderStat
(
   channelId
);

/*==============================================================*/
/* Index: IDX_TicketNormalOrder_filmId                          */
/*==============================================================*/
create index IDX_TicketNormalOrder_filmId on CEC_TicketOrderStat
(
   filmId
);

/*==============================================================*/
/* Index: IDX_TicketNormalOrder_showType                        */
/*==============================================================*/
create index IDX_TicketNormalOrder_showType on CEC_TicketOrderStat
(
   showType
);

/*==============================================================*/
/* Index: IDX_TicketNormalOrder_specialPolicyId                 */
/*==============================================================*/
create index IDX_TicketNormalOrder_specialPolicyId on CEC_TicketOrderStat
(
   specialPolicyId
);

/*==============================================================*/
/* Table: CEC_TicketSettings                                    */
/*==============================================================*/
create table CEC_TicketSettings
(
   id                   char(36) not null,
   showTypes            varchar(60) not null,
   printMode            varchar(3) not null,
   voucherCodeLength    int not null,
   syncShowDays         int not null,
   keepShowDays         smallint not null,
   accessTypeId         char(36) not null,
   logLength            int not null,
   url                  varchar(800),
   username             varchar(20),
   password             varchar(60),
   params               varchar(120),
   syncShowInterval     smallint not null,
   lastSyncShowTime     datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_TicketVoucher                                     */
/*==============================================================*/
create table CEC_TicketVoucher
(
   id                   char(36) not null,
   code                 varchar(60) not null,
   printCode            varchar(20),
   verifyCode           varchar(20),
   status               varchar(3) not null comment '0.未验证 1.已验证',
   printable            bool not null,
   reprintCount         int not null,
   genTime              datetime not null,
   confirmPrintTime     datetime,
   expireTime           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_User                                              */
/*==============================================================*/
create table CEC_User
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   username             varchar(20) not null,
   password             varchar(120) not null,
   enabled              bool not null comment '0.停用 1.启用',
   ordinal              int,
   creatorId            char(36) not null,
   createDate           datetime not null,
   modifierId           char(36) not null,
   modifyDate           datetime not null,
   primary key (id)
);

/*==============================================================*/
/* Table: CEC_UserSettings                                      */
/*==============================================================*/
create table CEC_UserSettings
(
   id                   char(36) not null,
   defaultActorId       char(36) not null,
   phone                varchar(20) not null,
   email                varchar(60) not null,
   receiveEmail         bool not null,
   receiveSms           bool not null,
   primary key (id)
);

alter table CEC_Actor add constraint FK_Actor_organId foreign key (organId)
      references CEC_Organ (id) on delete cascade on update restrict;

alter table CEC_Actor add constraint FK_Actor_roleId foreign key (roleId)
      references CEC_Role (id) on delete cascade on update restrict;

alter table CEC_Actor add constraint FK_Actor_userId foreign key (userId)
      references CEC_User (id) on delete cascade on update restrict;

alter table CEC_BenefitCard add constraint FK_BenefitCard_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete restrict on update restrict;

alter table CEC_BenefitCard add constraint FK_BenefitCard_userId foreign key (userId)
      references CEC_BenefitCardUser (id) on delete restrict on update restrict;

alter table CEC_BenefitCardTypeLog add constraint FK_BenefitCardTypeLog_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete restrict on update restrict;

alter table CEC_BenefitCardTypeRule add constraint FK_BenefitCardTypeRule_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete restrict on update restrict;

alter table CEC_BenefitCardTypeSnackRule add constraint FK_BenefitCardTypeSnackRule_typeId foreign key (typeId)
      references CEC_BenefitCardType (id) on delete cascade on update restrict;

alter table CEC_ChannelPolicyLog add constraint FK_ChannelPolicyLog_policyId foreign key (policyId)
      references CEC_ChannelPolicy (id) on delete cascade on update restrict;

alter table CEC_ChannelRule add constraint FK_ChannelCinemaRule_policyId foreign key (groupId)
      references CEC_ChannelRuleGroup (id) on delete cascade on update restrict;

alter table CEC_ChannelRuleGroup add constraint FK_ChannelRuleGroup_policyId foreign key (policyId)
      references CEC_ChannelPolicy (id) on delete cascade on update restrict;

alter table CEC_CinemaPolicyLog add constraint FK_CinemaPolicyLog_policyId foreign key (policyId)
      references CEC_CinemaPolicy (id) on delete cascade on update restrict;

alter table CEC_CinemaRule add constraint FK_CinemaRule_policyId foreign key (policyId)
      references CEC_CinemaPolicy (id) on delete cascade on update restrict;

alter table CEC_FilmErrorLog add constraint FK_MovieErrorLog_syncLogId foreign key (syncLogId)
      references CEC_FilmSyncLog (id) on delete cascade on update restrict;

alter table CEC_Hall add constraint FK_Hall_cinemaId foreign key (cinemaId)
      references CEC_Cinema (id) on delete cascade on update restrict;

alter table CEC_MinPriceGroup add constraint FK_MinPriceGroup_filmId foreign key (filmId)
      references CEC_Film (id) on delete cascade on update restrict;

alter table CEC_Organ add constraint FK_Organ_parentId foreign key (parentId)
      references CEC_Organ (id) on delete cascade on update restrict;

alter table CEC_Seat add constraint FK_Seat_hallId foreign key (hallId)
      references CEC_Hall (id) on delete cascade on update restrict;

alter table CEC_ShowErrorLog add constraint FK_ShowErrorLog_syncLogId foreign key (syncLogId)
      references CEC_ShowSyncLog (id) on delete cascade on update restrict;

alter table CEC_ShowUpdateLog add constraint FK_ShowUpdateLog_showId foreign key (showId)
      references CEC_Show (id) on delete cascade on update restrict;

alter table CEC_Snack add constraint FK_Snack_typeId foreign key (typeId)
      references CEC_SnackType (id) on delete cascade on update restrict;

alter table CEC_SnackOrderItem add constraint FK_SnackOrderItem_orderId foreign key (orderId)
      references CEC_SnackOrder (id) on delete restrict on update restrict;

alter table CEC_SnackType add constraint FK_SnackType_groupId foreign key (groupId)
      references CEC_SnackGroup (id) on delete restrict on update restrict;

alter table CEC_SpecialChannel add constraint FK_SpecialChannel_ruleId foreign key (ruleId)
      references CEC_SpecialRule (id) on delete cascade on update restrict;

alter table CEC_SpecialPolicyLog add constraint FK_SpecialPolicyLog_policyId foreign key (policyId)
      references CEC_SpecialPolicy (id) on delete cascade on update restrict;

alter table CEC_SpecialRule add constraint FK_SpecialRule_policyId foreign key (policyId)
      references CEC_SpecialPolicy (id) on delete cascade on update restrict;

alter table CEC_TicketOrderItem add constraint FK_TicketOrderItem_orderId foreign key (orderId)
      references CEC_TicketOrder (id) on delete cascade on update restrict;

