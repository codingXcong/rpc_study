package cn.zgc.custom.RPConHttp.util;

public enum Encode {
	UTF8((byte)1);
	
	byte val;
	
	Encode(byte val){
		this.val = val;
	}

	public byte getVal() {
		return val;
	}

	public void setVal(byte val) {
		this.val = val;
	}
}
