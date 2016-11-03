package cn.zgc.socket.book.ch02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 设定服务器的连接请求队列长度,这里指的是阻塞在队列的长度
 * @author gczhang
 */
public class SocketConnQueueSize {
	public static void main(String[] args) throws IOException, InterruptedException {
		//指定连接请求队列长度为2
		ServerSocket serverSocket = new ServerSocket(9000, 2);
		Runnable r = new SocketClientThread();
		new Thread(r).start();
		//打开下面的注释，运行程序不会报错，从而表明：这里指的是阻塞在队列的长度
		while(true){
			Socket socket = serverSocket.accept();
			System.out.println("a new socket ip:"+socket.getInetAddress());
		}
		
	}
	
	static class SocketClientThread implements Runnable{

		@Override
		public void run() {
			try {
				Socket s1 = new Socket("localhost",9000);
				System.out.println("第一次连接成功！");
				Thread.currentThread().sleep(2000);
				Socket s2 = new Socket("localhost",9000);
				System.out.println("第二次连接成功！");
				Thread.currentThread().sleep(2000);
				Socket s3 = new Socket("localhost",9000);
				System.out.println("第三次连接成功！");
			}catch (InterruptedException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
