package cn.zgc.webservice._native.server;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * 原生webservice,不使用任何框架,使用JDK的api即可实现
 * 服务端
 * @author gczhang
 */
@WebService
public class PhoneService {
	public Phone getPhoneInfo(String osName) {
		Phone phone = new Phone();
		if (osName.endsWith("android")) {
			phone.setName("android");
			phone.setOwner("google");
			phone.setTotal(80);
		} else if (osName.endsWith("ios")) {
			phone.setName("ios");
			phone.setOwner("apple");
			phone.setTotal(15);
		} else {
			phone.setName("windows phone");
			phone.setOwner("microsoft");
			phone.setTotal(5);
		}
		return phone;
	}
	
	public void sayHello(String city){
        System.out.println("你好："+city);
    }
    private void sayLuck(String city){
        System.out.println("好友："+city);
    }
    
    public static void main(String[] args) {
		String address = "http://127.0.0.1:9000/phoneService";
		//将服务提供出去
		//1、address 服务发布的地址
		//2、new PhoneService()提供服务的实现类
		Endpoint.publish(address, new PhoneService());
		System.out.println("wsdl的地址:"+address+"?wsdl");
	}
}
