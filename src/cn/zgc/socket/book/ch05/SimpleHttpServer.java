package cn.zgc.socket.book.ch05;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单模拟支持Http协议的服务端
 * @author gczhang
 *
 */
public class SimpleHttpServer {
	private ServerSocketChannel ssc;
	private int port = 9000;
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		new SimpleHttpServer().service();
	}
	public void service() throws IOException{
		ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(port));
		System.out.println("服务器启动了...");
		
		ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		while(true){
			SocketChannel socketChannel = ssc.accept();
			System.out.println("客户端连接来了，IP："+socketChannel.socket().getInetAddress()
					+"port:"+socketChannel.socket().getPort());
			threadPool.execute(new Task(socketChannel));
		}
		
	}
	
	class Task implements Runnable {
		SocketChannel socketChannel;

		public Task(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		@Override
		public void run() {
			processReq(socketChannel);
		}

		private void processReq(SocketChannel socketChannel) {
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			try {
				socketChannel.read(readBuffer);
				// 使用Buffer的时候，读和写之间要调用一下flip方法
				readBuffer.flip();
				String request = Charset.forName("utf-8").decode(readBuffer).toString();
				System.out.println(request);
				
				// 输出响应
				StringBuilder sb = new StringBuilder("HTTP/1.1 200 OK\r\n");
				sb.append("Content-Type:text/html\r\n\r\n");
				socketChannel.write(Charset.forName("utf-8").encode(sb.toString()));
				
				FileInputStream in = null;
		        //获得HTTP请求的第一行
				if(request!=null&&!"".equals(request)){
					String firstLineOfRequest=request.substring(0,request.indexOf("\r\n"));
			        if(firstLineOfRequest.indexOf("login.htm")!=-1)
			           in=new FileInputStream("login.htm");
			        else
			           in=new FileInputStream("hello.htm");
				}
		        FileChannel fileChannel=in.getChannel();
		        fileChannel.transferTo(0,fileChannel.size(),socketChannel);
		        fileChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				/*try {
					System.out.println("...连接关闭了");
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		}

	}
}
