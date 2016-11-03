package cn.zgc.socket.book.ch02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * socket连接的超时时间
 * @author gczhang
 */
public class SocketTimeOutTest {
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		Socket socket = new Socket();
		SocketAddress endpoint = new InetSocketAddress("www.baidu.com",80);
		try {
			socket.connect(endpoint , 2000);  //设置连接的超时时间为1秒
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-startTime);
	}
}
