-- 创建接入类型
insert into CEC_TicketAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate)
values ('TICACCID-0000-0000-0000-000000000001', '火烈鸟1.5', '1', '020', '2', '', '', '', 50, 50,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_TicketAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate)
values ('TICACCID-0000-0000-0000-000000000002', '鼎新1.0', '2', '040', '1', 'http://api.platform.yinghezhong.com', '10128', 'zy.89bud20dniw', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_TicketAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout,creatorId, createDate, modifierId, modifyDate)
values ('TICACCID-0000-0000-0000-000000000003', '火凤凰特供0.4', '3', '050', '1', 'http://211.147.239.210:8080/v4csharp/DI_DataSender_For_BOM.asmx', 'DAT', '888888', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_TicketAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate)
values ('TICACCID-0000-0000-0000-000000000004', '满天星5.0.1', '4', '060', '1', 'http://ticket.mvtapi.com:8500/ticketapi/services/ticketapi', 'TEST', '12345678', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());

-- 创建会员类型。
insert into CEC_MemberAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate) 
values ('MEMACCID-0000-0000-0000-000000000002', '火烈鸟1.5', '1', '020', '2', '', '', '', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_MemberAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate)
values ('MEMACCID-0000-0000-0000-000000000001', '鼎新1.0', '2', '040', '2', '', '', '', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_MemberAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate) 
values ('MEMACCID-0000-0000-0000-000000000003', '火凤凰特供0.4', '3', '050', '2', '', '', '', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());
insert into CEC_MemberAccessType (id, name, provider, adapter, model, url, username, password, socketTimeout, connectTimeout, creatorId, createDate, modifierId, modifyDate)
values ('MEMACCID-0000-0000-0000-000000000004', '满天星5.0.1', '4', '060', '2', '', '', '', 10, 5,'ADMINUID-0000-0000-0000-000000000000', NOW(), 'ADMINUID-0000-0000-0000-000000000000', NOW());

INSERT INTO `CEC_Cinema` VALUES ('0d958a27-51ab-4856-ab17-40b97183f7da', 'gzxxx', '满天星', '4', '9', '440103', '广州市荔湾区华嘉购物中心四楼', null, 'http://www.cfc.com.cn/', '123456789', '', null, '8.00', 1,'', '', '113.217621', '23.080649', '', '1', '1', '1', '0', 'ADMINUID-0000-0000-0000-000000000000', '2014-09-30 17:41:16', 'ADMINUID-0000-0000-0000-000000000000', '2014-10-09 17:22:59');
INSERT INTO `CEC_Cinema` VALUES ('77987181-0fdf-4a02-af14-07d87fc6fc94', '01010071', '中影益田(火烈鸟)影院', '1', '4', '440304', '深圳市福田区金地工业区111栋四楼', null, 'http://www.cfc.com.cn/', '07551234567', '', null, '8.00', 1,'', '', '', '', '', '1', '1', '1', '1', 'ADMINUID-0000-0000-0000-000000000000', '2014-09-30 15:42:21', 'ADMINUID-0000-0000-0000-000000000000', '2014-10-09 17:23:05');
INSERT INTO `CEC_Cinema` VALUES ('9b8d3c7b-89b3-476e-b326-de629188df59', '11', '鼎新', '2', '19', '110108', '北京市海淀区航天桥科原大厦', null, 'http://www.cfc.com.cn/', '123456789', '', null, '8.00', 1,'', '', '116.315831', '39.93488', '', '1', '1', '1', '0', 'ADMINUID-0000-0000-0000-000000000000', '2014-09-30 17:40:38', 'ADMINUID-0000-0000-0000-000000000000', '2014-10-09 17:22:47');
INSERT INTO `CEC_Cinema` VALUES ('cec9c633-76ba-421b-95d7-c9731b260972', '11051601', '火凤凰', '3', '0', '310101', '上海市黄浦区太阳都市花园', null, 'http://www.cfc.com.cn/', '123456789', '', null, '8.00', 1,'', '', '121.495226', '31.228966', '', '1', '1', '1', '0', 'ADMINUID-0000-0000-0000-000000000000', '2014-09-30 17:43:55', 'ADMINUID-0000-0000-0000-000000000000', '2014-10-09 17:22:54');

INSERT INTO `CEC_TicketSettings` VALUES ('77987181-0fdf-4a02-af14-07d87fc6fc94', '1,2,3,4', '1', '6', '3', '7', 'TICACCID-0000-0000-0000-000000000001', 1000, 'http://172.16.34.6:9080/scts.service/webservice/query', '01010071', '123456', null, 30, null);
INSERT INTO `CEC_TicketSettings` VALUES ('9b8d3c7b-89b3-476e-b326-de629188df59', '1,2,3,4', '1', '6', '3', '7', 'TICACCID-0000-0000-0000-000000000002', 1000, '', '', '', null, 30, null);
INSERT INTO `CEC_TicketSettings` VALUES ('cec9c633-76ba-421b-95d7-c9731b260972', '1,2,3,4', '1', '6', '3', '7', 'TICACCID-0000-0000-0000-000000000003', 1000, '', '', '', 'cinemaLinkId=999', 30, null);
INSERT INTO `CEC_TicketSettings` VALUES ('0d958a27-51ab-4856-ab17-40b97183f7da', '1,2,3,4', '1', '6', '3', '7', 'TICACCID-0000-0000-0000-000000000004', 1000, '', '', '', null, 30, null);

INSERT INTO `CEC_MemberSettings` VALUES ('0d958a27-51ab-4856-ab17-40b97183f7da', 'MEMACCID-0000-0000-0000-000000000004', 1000, 'http://member.mvtapi.com:8310/cmtspay/services/payapi?wsdl', 'cmtsxx', '123456', null);
INSERT INTO `CEC_MemberSettings` VALUES ('77987181-0fdf-4a02-af14-07d87fc6fc94', 'MEMACCID-0000-0000-0000-000000000002', 1000, 'http://172.16.34.6:9080/scts.service/webservice/pay?wsdl', '999001', '123456', null);
INSERT INTO `CEC_MemberSettings` VALUES ('9b8d3c7b-89b3-476e-b326-de629188df59', 'MEMACCID-0000-0000-0000-000000000003', 1000, 'http://mapi.platform.yinghezhong.com', '10128', 'zy.muielsH9niw', null);
INSERT INTO `CEC_MemberSettings` VALUES ('cec9c633-76ba-421b-95d7-c9731b260972', 'MEMACCID-0000-0000-0000-000000000001', 1000, 'http://211.147.239.210:8080/v4csharp/DI_DataSender_For_BOM.asmx', 'DAT', '888888', null);
