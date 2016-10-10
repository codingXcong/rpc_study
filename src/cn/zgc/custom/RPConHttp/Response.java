package cn.zgc.custom.RPConHttp;

public class Response {
	private byte encode;
	/*
	 * 响应 文本
	 */
	private String response;
	private String responseLength;
	public byte getEncode() {
		return encode;
	}
	public String getResponse() {
		return response;
	}
	public String getResponseLength() {
		return responseLength;
	}
	public void setEncode(byte encode) {
		this.encode = encode;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public void setResponseLength(String responseLength) {
		this.responseLength = responseLength;
	}
	
}
