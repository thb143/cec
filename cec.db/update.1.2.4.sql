ALTER TABLE `CEC_TicketOrderStat`
DROP INDEX `IDX_TicketNormalOrder_cinemaId`,
DROP INDEX `IDX_TicketNormalOrder_channelId`,
DROP INDEX `IDX_TicketNormalOrder_filmId`,
DROP INDEX `IDX_TicketNormalOrder_showType`,
DROP INDEX `IDX_TicketNormalOrder_specialPolicyId`;

-- 新增影院排序字段
alter table CEC_Cinema add ordinal int not null default 0;