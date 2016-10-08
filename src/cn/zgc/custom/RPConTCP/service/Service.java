package cn.zgc.custom.RPConTCP.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cn.zgc.custom.RPConTCP.HelloServiceImpl;

public class Service{
	
	private static Map<String,Object> services = new HashMap<String,Object>();
	
	static{
		services.put("cn.zgc.custom.RPConTCP.IHelloService", new HelloServiceImpl());
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ServerSocket ss = new ServerSocket(9999);
		while(true){
			Socket socket = ss.accept();
			System.out.println("来了一个连接....");
			InputStream in = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(in);
			String interfaceName = ois.readUTF();
			String methodName = ois.readUTF();
			Class<?>[] paramsType = (Class<?>[]) ois.readObject();
			String params =  (String) ois.readObject();
			
			Class serviceInterface = Class.forName(interfaceName);
			Object service = services.get(interfaceName);
			
			Method method = serviceInterface.getMethod(methodName, paramsType);
			Object result = method.invoke(service, params);
			
			OutputStream out =  socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(result);
		}
	}
}
