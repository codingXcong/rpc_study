package cn.zgc.custom.RPConTCP;

public class HelloServiceImpl implements IHelloService {

	@Override
	public String hello(String msg) {
		return "你好，"+msg+"! 我是自定义的rpc框架哦~~~";
	}

}
