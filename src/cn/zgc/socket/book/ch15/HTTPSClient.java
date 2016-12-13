package cn.zgc.socket.book.ch15;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
/**
 * 访问HTTPS站点,运行速度较http站点要慢
 */
public class HTTPSClient {
	SSLSocketFactory factory;
	SSLSocket sslSocket;
	String host = "www.baidu.com";
	
	public HTTPSClient() throws IOException{
		factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		sslSocket = (SSLSocket) factory.createSocket(host, 443);
		// 返回支持的加密套件
		String[] scs = sslSocket.getSupportedCipherSuites();
		// 返回支持的加密传输协议
		String[] sp = sslSocket.getSupportedProtocols();
		sslSocket.setEnabledCipherSuites(scs);
		// ？？？切换不同的https站点，输出同样。
		System.out.println(scs.length+":"+Arrays.toString(scs));
		System.out.println(sp.length+":"+Arrays.toString(sp));
		
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
				System.out.println(baos.toString());
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
	
	public static void main(String[] args) throws IOException {
		new HTTPSClient().communicate();
	}
}
