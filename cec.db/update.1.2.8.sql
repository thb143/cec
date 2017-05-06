-- 创建临时表用于合成订单表和渠道排期表
create table Tmp_TicketOrder select t.*,c.cinemaId,c.hallId,c.channelId,c.filmId,
c.provider,c.showCode,c.code as channelShowCode,c.filmCode,c.language,c.showType,c.showTime,
c.duration,c.stopSellTime,c.stopRevokeTime,c.minPrice,c.stdPrice,c.cinemaRuleId,
c.channelRuleId,c.specialRuleId,c.specialChannelId,c.cinemaPrice as singleCinemaPrice,c.
channelPrice as singleChannelPrice,c.submitPrice as singleSubmitPrice,c.connectFee as singleConnectFee,
c.circuitFee as singleCircuitFee,c.subsidyFee as singleSubsidyFee,c.createDate as channelShowCreateDate,
c.modifyDate as channelShowModifyDate from CEC_TicketOrder t,CEC_ChannelShow c where t.channelShowId = c.id;
-- 删除渠道排期ID字段
alter table Tmp_TicketOrder drop channelShowId;
-- 删除订单明细和订单表之间的外键约束
alter table CEC_TicketOrderItem drop foreign key FK_TicketOrderItem_orderId;
alter table CEC_OrderSendRecord drop foreign key FK_OrderSendRecord_orderId;
-- 删除原订单表
drop table CEC_TicketOrder;
-- 将临时订单表改名为订单表
rename table Tmp_TicketOrder to CEC_TicketOrder;
-- 设置主键和索引
alter table CEC_TicketOrder add primary key (id);
alter table CEC_TicketOrder add unique IDX_TicketOrder_code (code);
-- 处理中影的没有找到关联订单的订单明细记录
delete from CEC_TicketOrderItem where orderId = '0d8863fa-66f4-4874-bcd0-a6374a143734';
-- 创建订单明细和订单表之间的外键约束
alter table CEC_TicketOrderItem add constraint FK_TicketOrderItem_orderId foreign key (orderId)
      references CEC_TicketOrder (id) on delete cascade on update restrict;
alter table CEC_OrderSendRecord add constraint FK_OrderSendRecord_orderId foreign key (orderId)
      references CEC_TicketOrder (id) on delete cascade on update restrict;

-- 增加排期ID字段
alter table CEC_ChannelShow add showId char(36);
-- 为排期编码和渠道排期编码创建索引，加快更新记录时的速度
alter table CEC_Show add index IDX_Show_code_tmp (code);
alter table CEC_ChannelShow add index IDX_ChannelShow_showCode_tmp (showCode);
-- 把排期编码对应的排期ID填充到新增的字段
update CEC_ChannelShow c set c.showId = (select s.id from CEC_Show s where c.cinemaId = s.cinemaId and s.code = c.showCode);
-- 删除之前创建的排期编码和渠道排期编码索引
alter table CEC_Show drop index IDX_Show_code_tmp;
alter table CEC_ChannelShow drop index IDX_ChannelShow_showCode_tmp;
-- 如果还存在排期ID为空的渠道排期记录，则删除。
delete from CEC_ChannelShow where showId is null;
-- 修改排期ID字段为非空
alter table CEC_ChannelShow modify column showId char(36) not null;