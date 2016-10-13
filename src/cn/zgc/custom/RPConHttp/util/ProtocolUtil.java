package cn.zgc.custom.RPConHttp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import cn.zgc.custom.RPConHttp.Request;
import cn.zgc.custom.RPConHttp.Response;

public class ProtocolUtil {
	public static Request readRequest(InputStream in) throws IOException{
		//读取协议编码
		byte[] encodeArr = new byte[1];
		in.read(encodeArr);
		byte encode = encodeArr[0];
		
		//读取命令长度
		byte[] commandLengthByte = new byte[4];
		in.read(commandLengthByte);
		int commandLength = ByteUtil.bytes2Int(commandLengthByte);
		
		//读取命令
		byte[] commandByte = new byte[commandLength];
		in.read(commandByte);
		String command = "";
		if(encode == Encode.GBK.getVal()){
			command = new String(commandByte,"GBK");
		}else{
			command = new String(commandByte, "utf8");
		}
		
		Request request = new Request();
		request.setEncode(encode);
		request.setCommandLength(commandLength);
		request.setCommand(command);
		return request;
	}
	
	public static Response readResponse(InputStream in) throws IOException{
		//读取协议编码
		byte[] encodeArr = new byte[1];
		in.read(encodeArr);
		byte encode = encodeArr[0];
		
		//读取命令长度
		byte[] commandLengthByte = new byte[4];
		in.read(commandLengthByte);
		int commandLength = ByteUtil.bytes2Int(commandLengthByte);
		
		//读取命令
		byte[] commandByte = new byte[commandLength];
		in.read(commandByte);
		String command = "";
		if(encode == Encode.GBK.getVal()){
			command = new String(commandByte,"GBK");
		}else{
			command = new String(commandByte, "utf8");
		}
		
		Response response = new Response();
		response.setEncode(encode);
		response.setResponseLength(response.getResponseLength());
		response.setResponse(command);
		return response;
	}
	
	public static void writeRequest(OutputStream out,Request req) throws IOException{
		out.write(req.getEncode());
		out.write(req.getCommandLength());
		if(req.getEncode() == Encode.GBK.getVal()){
			out.write(req.getCommand().getBytes("gbk"));
		} else{
			out.write(req.getCommand().getBytes("utf8"));
		}
		out.flush();
	}
	
	public static void writeResponse(OutputStream out,Response response) throws IOException{
		out.write(response.getEncode());
		out.write(ByteUtil.int2Byte(response.getResponseLength()));
		if(response.getEncode() == Encode.GBK.getVal()){
			out.write(response.getResponse().getBytes("gbk"));
		} else{
			out.write(response.getResponse().getBytes("utf8"));
		}
		out.flush();
	}
}
