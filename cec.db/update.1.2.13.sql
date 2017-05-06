-- 规则分组新增“是否生效”字段，当分组下没有未生效的规则时，为true，否则为false，用于渠道结算策略页面展示的优化
ALTER TABLE CEC_ChannelRuleGroup ADD valid tinyint NOT NULL DEFAULT 1;
update CEC_ChannelRuleGroup t SET t.valid = IF((SELECT count(r.id) from CEC_ChannelRule r WHERE r.valid = '0' AND r.groupId = t.id) > 0, 0, 1 );