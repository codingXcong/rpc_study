package cn.zgc.socket.book.ch03;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 设置等待socket连接的超时时间
 * @author gczhang
 */
public class ServerSocketTimeOut {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(9000);
		/*
		 * 设置accept()方法的阻塞时间为5秒
		 * 如果5秒钟当中没有客户端连接，则抛出：
		 * Exception in thread "main" java.net.SocketTimeoutException: Accept timed out
		 */
		serverSocket.setSoTimeout(5000);
		Socket socket = serverSocket.accept();
		System.out.println("client..");
	}
}
