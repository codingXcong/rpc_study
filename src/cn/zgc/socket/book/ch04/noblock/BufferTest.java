package cn.zgc.socket.book.ch04.noblock;

import java.nio.CharBuffer;

public class BufferTest {
	public static void main(String[] args) {
		CharBuffer cb = CharBuffer.allocate(24);
		cb.put("1234567890");
		/*cb.flip();
		char a = cb.get();
		System.out.println(a);*/
		cb.compact();
		char a = cb.get();
		System.out.println(a);
		int limit = cb.limit();
		int pos = cb.position();
		System.out.println("limit:"+limit+" pos:"+pos);
	}
}
