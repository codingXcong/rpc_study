package cn.zgc.socket.book.ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/**
 * 演示客户端和服务器端通信时，如果有一方突然结束程序，或者关闭socket，或者单独关闭了输入流或输出流，对另一方会造成什么影响。
 * @author gczhang
 *
 */
public class Receiver {
	private static int stopWay = 1;
	
	public static void main(String[] args) {
		if(args.length>0) stopWay = Integer.parseInt(args[0]);
		new Receiver().receive();
	}
	
	public void receive(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(8000);
			socket = serverSocket.accept();
			System.out.println(socket);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			for(int i=0; i<20; i++){
				String msg = br.readLine();
				System.out.println("receive: "+msg+"|"+new Date());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i==2){
					if(stopWay == 2){    //突然终止程序
						System.out.println("突然终止程序");
						System.exit(0);
						break;
					} else if(stopWay == 3){   //关闭socket
						System.out.println("关闭socket");
						socket.close();
						break;
					} else if(stopWay == 4){
						System.out.println("关闭输出流");
						socket.shutdownOutput();
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(serverSocket!=null)
					serverSocket.close();
				if(socket!=null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
