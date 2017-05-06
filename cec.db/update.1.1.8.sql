-- 修改排期同步日志错误记录表
alter table CEC_ShowErrorLog modify column showCode varchar(60);

-- 修改会员接入类型表
ALTER TABLE CEC_MemberAccessType
 ADD name VARCHAR(20) NOT NULL AFTER id;
ALTER TABLE CEC_TicketAccessType
 ADD params VARCHAR(200) AFTER password;

UPDATE CEC_MemberAccessType SET name = '火烈鸟1.5' WHERE id = 'MEMACCID-0000-0000-0000-000000000001';
UPDATE CEC_MemberAccessType SET name = '鼎新1.0' WHERE id = 'MEMACCID-0000-0000-0000-000000000002';
UPDATE CEC_MemberAccessType SET name = '火凤凰特供0.4' WHERE id = 'MEMACCID-0000-0000-0000-000000000003';
UPDATE CEC_MemberAccessType SET name = '满天星5.0.1' WHERE id = 'MEMACCID-0000-0000-0000-000000000004';

-- 修改选座票接入类型表
ALTER TABLE CEC_TicketAccessType
 ADD name VARCHAR(20) NOT NULL AFTER id;
ALTER TABLE CEC_MemberAccessType
 ADD params VARCHAR(200) AFTER password;

UPDATE CEC_TicketAccessType SET name = '火烈鸟国标2013' WHERE id = 'TICACCID-0000-0000-0000-000000000001';
UPDATE CEC_TicketAccessType SET name = '鼎新1.0' WHERE id = 'TICACCID-0000-0000-0000-000000000002';
UPDATE CEC_TicketAccessType SET name = '火凤凰特供0.4' WHERE id = 'TICACCID-0000-0000-0000-000000000003';
UPDATE CEC_TicketAccessType SET name = '满天星5.0.1' WHERE id = 'TICACCID-0000-0000-0000-000000000004';

