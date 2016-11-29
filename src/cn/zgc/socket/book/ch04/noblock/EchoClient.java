package cn.zgc.socket.book.ch04.noblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 通过SocketChannel，实现非阻塞通信
 * 
 * @author gczhang
 */
public class EchoClient {
	private SocketChannel socketChannel;
	private Selector selector;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

	public static void main(String[] args) throws IOException {
		final EchoClient client = new EchoClient();
		Thread receiver = new Thread() {
			public void run() {
				client.receiveFromUser();
			}
		};

		receiver.start();
		client.talk();
	}

	public void receiveFromUser() {
		try {
			BufferedReader localBr = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			while ((msg = localBr.readLine()) != null) {
				synchronized (sendBuffer) {
					sendBuffer.put(encode(msg + "\r\n"));
				}
				if (msg.equals("bye"))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void talk() throws IOException {
		socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		while (selector.select() > 0) {
			Set selectionKeys = selector.selectedKeys();
			Iterator itr = selectionKeys.iterator();
			while (itr.hasNext()) {
				SelectionKey key = null;
				try {
					key = (SelectionKey) itr.next();
					if (key.isWritable()) {
						send(key);
					} 
					if (key.isReadable()) {
						receive(key);
					}
					itr.remove();
				} catch (Exception e) {
					e.printStackTrace();
					try {
						if (key != null) {
							key.cancel();
							key.channel().close();
						}
					} catch (Exception ex) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void send(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		synchronized (sendBuffer) {
			sendBuffer.flip(); // 把极限设为位置,切换到读模式
			socketChannel.write(sendBuffer);
			sendBuffer.compact();
		}
	}

	public void receive(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.read(receiveBuffer);
		receiveBuffer.flip();
		String receiveData = decode(receiveBuffer);

		if (receiveData.indexOf("\n") == -1)
			return;

		String outputData = receiveData.substring(0, receiveData.indexOf("\n") + 1);
		System.out.print(outputData);
		if (outputData.equals("echo:bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与服务器的连接");
			selector.close();
			System.exit(0);
		}

		ByteBuffer temp = encode(outputData);
		receiveBuffer.position(temp.limit());
		receiveBuffer.compact();
	}

	public EchoClient() throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000));
		socketChannel.configureBlocking(false);
		System.out.println("与127.0.0.1：9000建立起连接");
		selector = Selector.open();
	}

	/**
	 * 解码
	 * 
	 * @param buffer
	 * @return
	 */
	public String decode(ByteBuffer buffer) {
		CharBuffer cBuffer = Charset.forName("utf-8").decode(buffer);
		return cBuffer.toString();
	}

	/**
	 * 编码
	 * 
	 * @param msg
	 * @return
	 */
	public ByteBuffer encode(String msg) {
		ByteBuffer buffer = Charset.forName("utf-8").encode(msg);
		return buffer;
	}
}
