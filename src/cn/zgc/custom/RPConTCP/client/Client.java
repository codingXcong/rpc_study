package cn.zgc.custom.RPConTCP.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import cn.zgc.custom.RPConTCP.IHelloService;


public class Client {
	
	
	
	public static void main(String[] args) throws Exception {
		//远程调用的接口
		String interfaceName = IHelloService.class.getName();
		//远程调用的方法
		Method method = IHelloService.class.getMethod("hello", String.class);
		//参数
		String param = "zgc";
		
		Socket socket = new Socket("127.0.0.1", 9999);
		OutputStream out = socket.getOutputStream();
		InputStream in = socket.getInputStream();
		//TODO 去除掉ObjectInputStream/ObjectOutputStream看是否还行
		
		
		ObjectOutputStream oos = new ObjectOutputStream(out);
		/**
		 * TODO
		 * 1、放这里会报错哦，回头研究
		 * ObjectInputStream ois = new ObjectInputStream(in);
		 */
		
		oos.writeUTF(interfaceName);
		oos.writeUTF(method.getName());
		oos.writeObject(method.getParameterTypes());
		oos.writeObject(param);
		
		ObjectInputStream ois = new ObjectInputStream(in);
		String result = (String) ois.readObject();
		System.out.println(result);
	}
}
