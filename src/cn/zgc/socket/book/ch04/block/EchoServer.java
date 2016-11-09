package cn.zgc.socket.book.ch04.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用ServerSocketChannel，阻塞
 * 这个例子只是用了ServerSocketChannel的外壳，实际用了还是ServerSocket、Socket
 * @author gczhang
 */
public class EchoServer {
	
	private ExecutorService executorService;    
	private ServerSocketChannel serverSocketChannel;
	
	public EchoServer() throws IOException{
		executorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors()*4);
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(9000));
		System.out.println("server 启动了");
	}
	
	public void service(){
		while(true){
			SocketChannel socketChannel = null;
			try {
				socketChannel = serverSocketChannel.accept();
				executorService.submit(new Handler(socketChannel));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new EchoServer().service();
	}
	
	class Handler implements Runnable{
		private SocketChannel socketChannel;
		
		public Handler(SocketChannel socketChannel){
			this.socketChannel = socketChannel;
		}
		
		@Override
		public void run() {
			try {
		        Socket socket=socketChannel.socket();
		        System.out.println("接收到客户连接，来自: " +
		        socket.getInetAddress() + ":" +socket.getPort());

		        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

		        String msg = null;
		        while ((msg = br.readLine()) != null) {
		          System.out.println(msg);
		          pw.println("echo:"+msg);
		          if (msg.equals("bye"))
		            break;
		        }
		      }catch (IOException e) {
		         e.printStackTrace();
		      }finally {
		         try{
		           if(socketChannel!=null)socketChannel.close();
		         }catch (IOException e) {e.printStackTrace();}
		      }
		}
		
	}
}
