package cn.zgc.socket.book.ch04.noblock;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 通过ServerSocketChannel，实现非阻塞通信
 * @author gczhang
 */
public class EchoServer {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	public EchoServer(){
		
	}
	
	public void service() throws IOException{
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	
	public static void main(String[] args) {
		
	}
}
