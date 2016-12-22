package cn.zgc.socket.book.ch15;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 通过握手完成监听（HandshakeCompletedListener）输出SSL握手过程产生的一些信息
 * @author gczhang
 */
public class HTTPSClientWithListener {
	private SSLSocket sslSocket;
	SSLSocketFactory factory;
	String host = "www.douyu.com"; 
	
	public void createSocket() throws Exception{
		factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		sslSocket = (SSLSocket) factory.createSocket(host, 443);
		sslSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
			
			@Override
			public void handshakeCompleted(HandshakeCompletedEvent event) {
				String cs = event.getCipherSuite();
				String remote = event.getSession().getPeerHost();
				System.out.println("加密套件为:"+cs);
				System.out.println("访问主机为:"+remote);
			}
		});
	}
	
	public void communicate(){
		StringBuilder sb = new StringBuilder();
		sb.append("GET / HTTP/1.1\r\n");
		sb.append("Host: "+host+"\r\n");
		sb.append("Accept: text/html,application/xml,image/webp,*/*\r\n");
		sb.append("\r\n");
		
		try {
			OutputStream os = sslSocket.getOutputStream();
			os.write(sb.toString().getBytes());
			InputStream in = sslSocket.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = in.read(buffer))!=-1){
				baos.write(buffer, 0, len);
				//System.out.println(baos.toString());
				baos.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sslSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		HTTPSClientWithListener client = new HTTPSClientWithListener();
		client.createSocket();
		client.communicate();
	}
}
