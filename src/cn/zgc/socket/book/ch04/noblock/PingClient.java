package cn.zgc.socket.book.ch04.noblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * 异步打印连接不同主机所耗费的时间
 * @author gczhang
 *
 */
public class PingClient {
	private Selector selector;
	// 存放用户新提交的任务
	private LinkedList targets = new LinkedList();
	// 存放已经完成的需要打印的任务
	private LinkedList finishedTargets = new LinkedList();

	public static void main(String[] args) throws IOException {
		new PingClient();
	}

	public PingClient() throws IOException {
		selector = Selector.open();
		Connector connector = new Connector();
		Printer printer = new Printer();
		connector.start();
		printer.start();
		receiveTarget();
	}

	// 接收用户输入的地址，向targets队列中加入任务
	private void receiveTarget() {
		try {
			BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			while ((msg = localReader.readLine()) != null) {
				if (!msg.equals("bye")) {
					Target target = new Target(msg);
					addTarget(target);
				} else {
					shutdown = true;
					selector.wakeup();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 向targets队列中加入一个任务
	private void addTarget(Target target) {
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(target.address);

			target.channel = socketChannel;
			target.connectStart = System.currentTimeMillis();

			synchronized (targets) {
				targets.add(target);
			}
			selector.wakeup();
		} catch (Exception x) {
			if (socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException xx) {
				}
			}
			target.failure = x;
			addFinishedTarget(target);
		}
	}

	// 打印finishedTargets队列中的任务
	private void printFinishedTargets() {
		try {
			while (true) {
				Target target = null;
				synchronized (finishedTargets) {
					while (finishedTargets.size() == 0)
						finishedTargets.wait();
					target = (Target) finishedTargets.removeFirst();
				}
				target.show();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public class Printer extends Thread {
		public Printer() {
			this.setDaemon(true);
		}

		@Override
		public void run() {
			printFinishedTargets();
		}
	}

	boolean shutdown = false;

	public class Connector extends Thread {
		@Override
		public void run() {
			while (!shutdown) {
				try {
					registerTargets();
					if (selector.select() > 0) {
						processSelectedKeys();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 取出targets队列中的任务，向Selector注册连接就绪事件
	public void registerTargets() {
		synchronized (targets) {
			while (targets.size() > 0) {
				Target target = (Target) targets.removeFirst();
				try {
					target.channel.register(selector, SelectionKey.OP_CONNECT, target);
				} catch (ClosedChannelException x) {
					try {
						target.channel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					target.failure = x;
					addFinishedTarget(target);
				}
			}
		}
	}

	// 向finishedTargets队列中加入一个任务
	private void addFinishedTarget(Target target) {
		synchronized (finishedTargets) {
			finishedTargets.notify();
			finishedTargets.add(target);
		}
	}

	// 处理连接就绪事件
	public void processSelectedKeys() throws IOException {
		Iterator itr = selector.selectedKeys().iterator();
		while (itr.hasNext()) {
			SelectionKey selectionKey = (SelectionKey) itr.next();
			itr.remove();
			Target target = (Target) selectionKey.attachment();
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

			try {
				if (socketChannel.finishConnect()) {
					selectionKey.cancel();
					target.connectFinish = System.currentTimeMillis();
					socketChannel.close();
					addFinishedTarget(target);
				}
			} catch (IOException e) {
				socketChannel.close();
				target.failure = e;
				addFinishedTarget(target);
			}
		}
	}
}

// 表示一项任务
class Target {
	InetSocketAddress address;
	SocketChannel channel;
	Exception failure;
	long connectStart; // 开始连接时的时间
	long connectFinish = 0; // 连接成功时的时间
	boolean shown = false; // 该任务是否已经打印

	public Target(String host) {
		try {
			address = new InetSocketAddress(host, 80);
		} catch (Exception e) {
			failure = e;
		}
	}

	void show() {
		String result;
		if (connectFinish != 0)
			result = Long.toString(connectFinish - connectStart) + "ms";
		else if (failure != null)
			result = failure.toString();
		else
			result = "Timed out";
		System.out.println(address + " : " + result);
		shown = true;
	}
}