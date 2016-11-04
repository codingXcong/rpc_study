package cn.zgc.socket.book.ch03.multithread2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerSocket使用Executors JDK自带线程池处理
 * 
 * @author gczhang
 */
public class EchoServer {
	private ServerSocket serverSocket;
	private static final int THREAD_SIZE_PER_PRO = 3; // 每个CPU核对应的线程数
	private ExecutorService threadPool; // 线程池

	public EchoServer() throws IOException {
		serverSocket = new ServerSocket(9000);
		threadPool = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors() * THREAD_SIZE_PER_PRO);
	}

	public static void main(String[] args) throws IOException {
		new EchoServer().service();
	}

	public void service() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			Runnable task = new Handler(socket);
			threadPool.execute(task);
		}
	}

	class Handler implements Runnable {
		Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("客户端连接了，ip:" + socket.getInetAddress() + " port" + socket.getPort());
			try {
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String msg = "";
				while ((msg = br.readLine()) != null) {
					System.out.println(msg);
					pw.println("echo:" + msg);
					if (msg.equals("bye")) {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
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
