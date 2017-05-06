-- 删除卖品凭证的过期时间
ALTER TABLE CEC_SnackVoucher DROP expireTime;

ALTER TABLE CEC_SnackOrder ADD discountAmount decimal(6,2) not null;
ALTER TABLE CEC_SnackOrderItem ADD discountPrice decimal(6,2) not null;
ALTER TABLE CEC_TicketOrder ADD discountAmount decimal(6,2) not null;
ALTER TABLE CEC_TicketOrderItem ADD discountPrice decimal(6,2) not null;

update CEC_TicketOrder c set c.discountAmount = (select bco.discountAmount from CEC_BenefitCardConsumeOrder bco where bco.id = c.id) where c.id in (select t.id from CEC_BenefitCardConsumeOrder t);
update CEC_TicketOrderItem c set c.discountPrice = (select bco.discountAmount/cto.ticketCount from CEC_BenefitCardConsumeOrder bco,CEC_TicketOrder cto where bco.id = cto.id AND bco.id = c.orderId) where c.orderId in (select t.id from CEC_BenefitCardConsumeOrder t);

