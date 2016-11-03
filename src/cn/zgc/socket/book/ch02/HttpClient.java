package cn.zgc.socket.book.ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 通过socket建立http协议
 * @author gczhang
 */
public class HttpClient {
	Socket socket = null;
	String host;
	int port;
	
	public HttpClient() throws IOException {
		this.host = "www.baidu.com";
		this.port = 80;
		socket = new Socket(host,port);
	}
	public HttpClient(String host, int port) throws IOException {
		super();
		this.host = host;
		this.port = port;
		socket = new Socket(host,port);
	}
	
	public void get() throws IOException{
		//构造Http请求行 +请求头
		StringBuilder sb = new StringBuilder("GET / HTTP/1.1\r\n");
		sb.append("Host: www.baidu.com\r\n");
		sb.append("Connection: keep-alive\r\n");
		sb.append("Accept-Encoding: gzip, deflate, sdch, br\r\n");
		sb.append("Accept-Language: zh-CN,zh;q=0.8\r\n");
		sb.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36\r\n");
		sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		
		//发送请求数据
		OutputStream out = socket.getOutputStream();
		out.write(sb.toString().getBytes("utf-8"));
		//socket.shutdownOutput();
		
		InputStream in = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String line;
		while((line = br.readLine())!=null){
			System.out.println(line);
		}
		//socket.close();
	}

	public static void main(String[] args) throws IOException {
		HttpClient client = new HttpClient();
		client.get();
	}
	
}
