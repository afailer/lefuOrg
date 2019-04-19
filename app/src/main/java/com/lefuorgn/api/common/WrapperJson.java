package com.lefuorgn.api.common;

import java.io.Serializable;

/**
 * 对于请求结果的封装结构(Json结构)
 */
public class WrapperJson implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 请求成功与否的状态码字段
	 */
	public String code_name = "code";
	/**
	 * 请求失败的状态码值，非此值均为成功
	 */
	public int code_error_value = -1;
	/**
	 * 请求超时,或者用户未登录状态
	 */
	public int code_overtime_value = 4;
	/**
	 * 参数错误,或者请求错误.如:手机号注册时,该手机已近注册
	 */
	public int code_parameter_error_value = 2;
	/**
	 * 未授权
	 */
	public int code_unauthorized_value = 401;
	/**
	 * 请求结果字段
	 */
	public String result_name = "data";
	/**
	 * 请求失败时的错误信息，仅在请求失败时该字段才有值
	 */
	public String error_name = "msg";
}
