package cn.zgc.custom.RPConHttp;

public class Response {
	private byte encode;
	/*
	 * 响应 文本
	 */
	private String response;
	private int responseLength;
	public byte getEncode() {
		return encode;
	}
	public String getResponse() {
		return response;
	}
	public int getResponseLength() {
		return responseLength;
	}
	public void setEncode(byte encode) {
		this.encode = encode;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}
	
}
