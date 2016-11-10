package cn.zgc.socket.book.ch04.noblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 通过ServerSocketChannel，实现非阻塞通信
 * @author gczhang
 */
public class EchoServer {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	public EchoServer() throws IOException{
		// 获取selector,监听器的作用
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		// 设置端口复用
		serverSocketChannel.socket().setReuseAddress(true);
		// 借助和ServerSocketChannel关联的ServerSocket进行端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(9000));
		// 设置ServerSocketChannel的模式为非阻塞模式，默认为阻塞
		serverSocketChannel.configureBlocking(false);
		System.out.println("Server started...");
	}
	
	public void service() throws IOException{
		// 注册OP_ACCEPT事件，当有客户端连接会触发selector
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		/*
		 * selector.select()为阻塞方法，方法返回当前共有多少个被监听的事件发生
		 */
		while(selector.select()>0){
			// Selector监听到的事件，且还没有被处理
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			// 遍历监听到的事件，取出进行处理
			Iterator it = selectionKeys.iterator();
			while(it.hasNext()){
				SelectionKey key = (SelectionKey) it.next();
				if(key.isAcceptable()){ //客户端连接
					
				}
				if(key.isReadable()) {  //可从流中读取数据
					
				}
				if(key.isWritable()) {  //可往流中写入数据
					
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(Charset.defaultCharset().displayName());
	}
}
