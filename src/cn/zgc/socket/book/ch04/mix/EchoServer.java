package cn.zgc.socket.book.ch04.mix;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
/**
 * 混合使用阻塞和非阻塞模式
 * 接收客户端连接使用阻塞模式（ServerSocketChannel采用默认的阻塞模式）
 * 发送和接受数据使用非阻塞模式
 * @author gczhang
 *
 */
public class EchoServer {
	
	private Selector selector;
	private ServerSocketChannel ssc;
	private int port = 9000;
	// 用在Buffer和String之间的转换
	private Charset charset = Charset.forName("utf-8");
	private Object gate=new Object();  //用作互斥锁
	
	public static void main(String[] args) throws IOException {
		//定义了两个线程，主线程负责接受和发送数据；子线程负责接收客户端连接
		final EchoServer server = new EchoServer();
	    Thread accept=new Thread(){
	        public void run(){
	            server.accept();
	        }
	    };
	    accept.start();
	    server.service();
	}
	
	public EchoServer()throws IOException{
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		ssc.socket().setReuseAddress(true);
		ssc.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动了");
	}
	
	public void accept(){
		for(;;){
			try{
				 //接收客户端连接
				 SocketChannel socketChannel = ssc.accept();
				 System.out.println("接收到客户连接，来自:" +
                         socketChannel.socket().getInetAddress() +
                         ":" + socketChannel.socket().getPort());
				 socketChannel.configureBlocking(false);  //socketChannel设置为非阻塞模式
				 ByteBuffer buffer = ByteBuffer.allocate(1024);
				 synchronized (gate) {
					selector.wakeup();  //使尚未返回的第一个选择操作立即返回
					// 向Selector注册读就绪和写就绪事件
					// SocketChannel.register和selector.select()方法都会操作Selector的keys集合，所以对共享资源的代码块
					//进行了同步，因此SocketChannel.register和selector.select()不会同时执行
					socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE, buffer);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public void service() throws IOException{
		while(true){
			synchronized (gate) {
				int n = selector.select();
			    if(n==0)continue;
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					it.remove();
					if (key.isReadable()) {
		                receive(key);
		            }
		            if (key.isWritable()) {
		                send(key);
		            }
				}
			}
		}
	}
	
	public void send(SelectionKey key)throws IOException{
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		buffer.flip();
		String data=charset.decode(buffer).toString();
	    if(data.indexOf("\n")==-1)return;
	    String outputData=data.substring(0,data.indexOf("\n")+1);
	    System.out.print(outputData);
	    ByteBuffer outputBuffer=charset.encode("echo:"+outputData);
	    while(outputBuffer.hasRemaining())
	      socketChannel.write(outputBuffer);

	    ByteBuffer temp=charset.encode(outputData);
	    buffer.position(temp.limit());
	    buffer.compact();

	    if(outputData.equals("bye\r\n")){
	      key.cancel();
	      socketChannel.close();
	      System.out.println("关闭与客户的连接");
	    }
	}
	
	public void receive(SelectionKey key)throws IOException{
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer readBuff= ByteBuffer.allocate(32);
	    socketChannel.read(readBuff);
	    readBuff.flip();
		buffer.limit(buffer.capacity());
	    buffer.put(readBuff);
	}
}
