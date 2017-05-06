package cn.mopon.cec.core.access.member.ng;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.core.jackson.GenericObjectMapper;

/**
 * NG会员响应对象基类。
 */
public abstract class NgReply {
	/** 返回码 */
	private String resultCode;
	/** 返回信息 */
	private String msg;
	@JsonIgnore
	protected ObjectMapper mapper = new GenericObjectMapper();

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String message) {
		this.msg = message;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
}