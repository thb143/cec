package cn.mopon.cec.core.access.member.ng;

import coo.base.exception.BusinessException;

/**
 * NG服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class NgServiceReply<T extends NgReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            相应对象
	 */
	public NgServiceReply(T reply) {
		this.reply = reply;
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param strJson
	 *            返回的json内容
	 * @throws Exception
	 *             抛出异常
	 */
	@SuppressWarnings("unchecked")
	public void genReply(String strJson) throws Exception {
		reply = (T) reply.getMapper().readValue(strJson, reply.getClass());
		if (!"000".equals(reply.getResultCode())) {
			throw new BusinessException(reply.getMsg());
		}
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}