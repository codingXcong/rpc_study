package cn.zgc.custom.RPConHttp.client;

import cn.zgc.custom.RPConHttp.Request;

public class Client {
	public static void main(String[] args) {
		Request request = new Request();
		request.setCommand("hello zgc!");
		request.setCommandLength(request.getCommand().length());
	}
}
