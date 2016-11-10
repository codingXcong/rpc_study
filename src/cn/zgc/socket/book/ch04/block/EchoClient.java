package cn.zgc.socket.book.ch04.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * SocketChannel,没有用到非阻塞特性
 * 
 * @author gczhang
 */
public class EchoClient {
	private SocketChannel socketChannel;

	public EchoClient() throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000));
	}

	public static void main(String[] args) throws IOException {
		new EchoClient().send();
	}

	public void send() throws IOException {
		BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		Socket socket = socketChannel.socket();
		BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(),true); 
		String iMsg = "";
		try {
			while ((iMsg = sin.readLine()) != null) {
				socketOut.println(iMsg);
				String respMsg = socketIn.readLine();
				System.out.println(respMsg);
				if (iMsg.equals("bye"))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
