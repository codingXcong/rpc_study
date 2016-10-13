package cn.zgc.custom.RPConHttp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import cn.zgc.custom.RPConHttp.Request;
import cn.zgc.custom.RPConHttp.Response;
import cn.zgc.custom.RPConHttp.util.Encode;
import cn.zgc.custom.RPConHttp.util.ProtocolUtil;

public class Client {
	public static void main(String[] args) throws IOException {
		Request request = new Request();
		request.setCommand("hello zgc!");
		request.setCommandLength(request.getCommand().length());
		request.setEncode(Encode.UTF8.getVal());
		
		Socket client = new Socket("127.0.0.1", 4567);
		OutputStream out = client.getOutputStream();
		InputStream in = client.getInputStream();
		
		ProtocolUtil.writeRequest(out, request);
		Response response = ProtocolUtil.readResponse(in);
		System.out.println(response.getResponse());
	}
}
