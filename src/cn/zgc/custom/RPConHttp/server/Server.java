package cn.zgc.custom.RPConHttp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cn.zgc.custom.RPConHttp.Request;
import cn.zgc.custom.RPConHttp.Response;
import cn.zgc.custom.RPConHttp.util.Encode;
import cn.zgc.custom.RPConHttp.util.ProtocolUtil;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(4567);
		System.out.println("server started....");
		while(true){
			Socket socket = serverSocket.accept();
			//客户端输入流
			InputStream in = socket.getInputStream();
			Request request = ProtocolUtil.readRequest(in);
			
			Response response = new Response();
			response.setEncode(Encode.UTF8.getVal());
			if(request.getCommand().equals("hello server")){
				response.setResponse("hello client");
			} else {
				response.setResponse("sorry,The command of "+request.getCommand()+"is not support!");
			}
			response.setResponseLength(response.getResponse().length());
			OutputStream out = socket.getOutputStream();
			ProtocolUtil.writeResponse(out, response);
		}
	}
}
