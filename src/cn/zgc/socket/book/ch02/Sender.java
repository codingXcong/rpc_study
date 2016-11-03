package cn.zgc.socket.book.ch02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 演示客户端和服务器端通信时，如果有一方突然结束程序，或者关闭socket，或者单独关闭了输入流或输出流，对另一方会造成什么影响。
 * @author gczhang
 */
public class Sender {
	private static int stopWay = 1;
	
	public static void main(String[] args) {
		if(args.length>0) stopWay = Integer.parseInt(args[0]);
		new Sender().send();
	}
	
	public void send() {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1",8000);
			System.out.println(socket);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
			for(int i=0; i<20; i++){
				String msg = "hello"+i;
				pw.write(msg);
				//pw.flush();
				System.out.println("send : "+msg+"|"+new Date());
				try {
					Thread.sleep(500);
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
		} finally {
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
