-- 权益卡规则增加“放映类型”字段，之前已有的规则，默认为全部放映类型
alter table CEC_BenefitCardTypeRule add showTypes varchar(60) not null;
update CEC_BenefitCardTypeRule set showTypes = '1,2,3,4,6';

--  权益卡增加“首次消费影院”字段和“出生日期”字段
alter table CEC_BenefitCard add birthday datetime;
alter table CEC_BenefitCard add firstCinemaId char(36);

UPDATE CEC_BenefitCard cbc
SET cbc.firstCinemaId = (
	SELECT
		cto.cinemaId
	FROM
		CEC_BenefitCardConsumeOrder bco
	LEFT JOIN CEC_TicketOrder cto ON cto.id = bco.id
	WHERE
		bco.cardId = cbc.id
	ORDER BY
		cto.confirmTime
	LIMIT 1
);

-- 影院结算策略增加报票房加减值字段
alter table CEC_CinemaPolicy add amount decimal(6,2) not NULL;
update CEC_CinemaPolicy set amount = 0;
alter table CEC_SpecialRule add amount decimal(6,2) not NULL;
update CEC_SpecialRule set amount = 0;