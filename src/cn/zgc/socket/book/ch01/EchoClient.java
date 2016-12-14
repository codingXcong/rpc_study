package cn.zgc.socket.book.ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Socket通信  客户端
 * @author gczhang
 *
 */
public class EchoClient {
	private Socket socket;
	
	public EchoClient() throws IOException{
		socket = new Socket("localhost",9999);
	}
	
	public void send(){
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out,true);
			BufferedReader localBr = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			while((line=localBr.readLine())!=null){
				pw.println(line);
				System.out.println("已发送："+line);
				String resp = br.readLine();
				System.out.println(resp);
				System.out.println("--------------------");
				if(line.equals("bye"))
					break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new EchoClient().send();
		
	}
}
