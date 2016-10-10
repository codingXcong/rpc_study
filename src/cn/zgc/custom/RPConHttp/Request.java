package cn.zgc.custom.RPConHttp;

public class Request {
	/*
	 * 协议编码 0:GBK 1:UTF-8
	 */
	private byte encode;
	/*
	 * 命令
	 */
	private String command;
	/*
	 * 命令长度
	 */
	private int commandLength;
	
	public byte getEncode() {
		return encode;
	}
	public String getCommand() {
		return command;
	}
	public int getCommandLength() {
		return commandLength;
	}
	public void setEncode(byte encode) {
		this.encode = encode;
	} 
	public void setCommand(String command) {
		this.command = command;
	}
	public void setCommandLength(int commandLength) {
		this.commandLength = commandLength;
	}
	
	
	
}
