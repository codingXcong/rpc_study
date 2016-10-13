package cn.zgc.custom.RPConHttp.util;

public class ByteUtil {
	
	public static int bytes2Int(byte[] bytes){
		int num = bytes[3] & 0xFF;
		num |= ((bytes[2]<<8) & 0xff00);
		num |= ((bytes[1]<<16) & 0xff0000);
		num |= ((bytes[0]<<24) & 0xff000000);
		return num;
	}
	
	public static byte[] int2Byte(int i){
		byte[] result = new byte[4];
		result[0] = (byte) ((i>>24) & 0xff);
		result[1] = (byte) ((i>>16) & 0xff);
		result[2] = (byte) ((i>>8) & 0xff);
		result[3] = (byte) (i & 0xff);
		return result;
	}
}
