package cn.zgc.socket.book.ch02;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 端口扫描，判断端口是否已经被服务器程序监听
 * @author gczhang
 */
public class PortScanner {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println(addr.getHostAddress());
		new PortScanner().scan("localhost");
	}
	
	
	public void scan(String host){
		Socket socket = null;
		for(int port=1; port<10000; port++){
			try {
				socket = new Socket(host,port);
				System.out.println("端口"+port+"被占用了");
			} catch (IOException e) {
				System.out.println("端口"+port+"没有被占用");
			} finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
