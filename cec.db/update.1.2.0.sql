-- 影片同步日志表删除最后更新时间字段
alter table CEC_FilmSyncLog drop column lastUpdateTime;

-- 规则分组 CEC_ChannelRuleGroup 表增加接入费字段connectFee
alter table CEC_ChannelRuleGroup add connectFee decimal(6,2) not null default 0;
-- 规则分组 CEC_ChannelRuleGroup 表中connectFee更新为原策略中的接入费
UPDATE CEC_ChannelRuleGroup t SET t.connectFee = (SELECT ccp.connectFee FROM CEC_ChannelPolicy ccp WHERE t.policyId = ccp.id);
-- 渠道结算策略 CEC_ChannelPolicy 表删除接入费字段connectFee
alter table CEC_ChannelPolicy drop column connectFee;


-- 影厅表中影厅类型改为可为空，长度为36
alter table CEC_Hall modify type CHAR(36);
-- 影厅表中影厅类型默认为空
update CEC_Hall set type = null ;
-- 新增影厅类型表
create table CEC_HallType
(
   id                   char(36) not null,
   name                 varchar(20) not null,
   primary key (id)
);