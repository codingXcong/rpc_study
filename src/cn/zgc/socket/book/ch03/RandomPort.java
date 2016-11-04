package cn.zgc.socket.book.ch03;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * ServerSocket一个由系统任意分配的端口
 * @author gczhang
 */
public class RandomPort {
	public static void main(String[] args) throws IOException {
		//当监听0端口时，系统就会随机分配一个端口给ServerSocket
		ServerSocket serverSocket = new ServerSocket(0);
		System.out.println(serverSocket.getLocalPort());
		
	}
}
