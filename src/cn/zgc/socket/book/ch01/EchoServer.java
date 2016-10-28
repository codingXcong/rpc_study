package cn.zgc.socket.book.ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket通信 服务端
 * @author gczhang
 */
public class EchoServer {
	private ServerSocket serverSocket;
	public EchoServer() throws IOException{
		//创建ServerSocket对象，监听9999端口
		serverSocket = new ServerSocket(9999);   
		System.out.println("Initialize socket server");
	}
	
	public void service() {
		while(true){
			Socket socket = null;
			try {
				// accept方法为阻塞方法，当有客户端连接就获取代表客户端的socket
				socket = serverSocket.accept();
				System.out.println("come a new connect:"+socket.getInetAddress());
				//获取输入流，从该流中读取客户端发送过来的数据
				InputStream in = socket.getInputStream();
				//获取输出流，往该流中写入数据即表示向客户端响应数据
				OutputStream out = socket.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(out),true);
				String line = null;
				while((line=br.readLine())!=null){
					System.out.println(line);
					pw.write("echo:"+line);
					pw.flush();
					if(line.equals("bye"))
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(socket!=null){
					try {
						//关闭socket，同时关闭对应的输入输出流
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		new EchoServer().service();
	}
}
