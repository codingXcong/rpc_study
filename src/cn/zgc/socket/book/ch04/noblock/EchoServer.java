package cn.zgc.socket.book.ch04.noblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 通过ServerSocketChannel，实现非阻塞通信
 * 
 * @author gczhang
 */
public class EchoServer {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;

	public EchoServer() throws IOException {
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

	public void service() throws IOException {
		// 注册OP_ACCEPT事件，当有客户端连接会触发selector
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		/*
		 * selector.select()为阻塞方法，会一直阻塞直到有事件发生。方法返回当前共有多少个被监听的事件发生
		 */
		while (selector.select() > 0) {
			// Selector监听到的事件，且还没有被处理
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			// 遍历监听到的事件，取出进行处理
			Iterator<SelectionKey> it = selectionKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				it.remove();
				if (key.isAcceptable()) { // 客户端连接
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					// 获得与客户连接的SocketChannel
					SocketChannel sc = ssc.accept();
					System.out.println("接收到客户连接，来自:" + sc.socket().getInetAddress() + ":" + sc.socket().getPort());
					// 指定SocketChannel为非阻塞模式
					sc.configureBlocking(false);
					// 存放用户发送过来的数据缓冲区
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					// SocketChannel注册读就绪和写就绪事件，且关联一个buffer附件
					sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, buffer);
				}
				if (key.isReadable()) { // 可从流中读取数据
					receive(key);
				}
				if (key.isWritable()) { // 可往流中写入数据
					send(key);
				}
			}
		}
	}
	
	/**
	 * 由于非阻塞读取的时候可能没读到一行就返回了，所以我们可以规定必须要读到一行数据服务端才响应
	 * @param key
	 * @throws IOException
	 */
	private void send(SelectionKey key) throws IOException {
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		buffer.flip(); // 把极限设为位置，把位置设为0
		String data = decode(buffer);
		// 如果没有读取到一行数据，server就先不返回
		if (data.indexOf("\r\n") == -1)
			return;
		// 一行数据
		String outputData = data.substring(0, data.indexOf("\n") + 1);
		System.out.print(outputData);
		// 响应数据
		ByteBuffer outputBuffer = encode("echo:" + outputData);
		// 在非阻塞模式下确保发送一行数据
		while (outputBuffer.hasRemaining())
			socketChannel.write(outputBuffer);

		ByteBuffer temp = encode(outputData);
		buffer.position(temp.limit());
		// 删除buffer中已经处理的数据
		buffer.compact();

		if (outputData.equals("bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与客户的连接");
		}
	}

	private void receive(SelectionKey key) throws IOException {
		// 获得与SelectionKey关联的附件
		ByteBuffer buffer=(ByteBuffer)key.attachment();
		// 获得与SelectionKey关联的SocketChannel
	    SocketChannel socketChannel=(SocketChannel)key.channel();
	    // 用于存放读取到的数据
	    ByteBuffer readBuff= ByteBuffer.allocate(32);
	    socketChannel.read(readBuff);
	    readBuff.flip();

	    buffer.limit(buffer.capacity());
	    buffer.put(readBuff);
	}

	public String decode(ByteBuffer buffer) { // 解码
		CharBuffer charBuffer = Charset.forName("utf-8").decode(buffer);
		return charBuffer.toString();
	}

	public ByteBuffer encode(String str) { // 编码
		return Charset.forName("utf-8").encode(str);
	}

	public static void main(String[] args) throws IOException {
		EchoServer server = new EchoServer();
	    server.service();
	}
}
