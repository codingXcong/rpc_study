package cn.zgc.webservice._native.client;

import cn.zgc.webservice._native.server.Phone;
import cn.zgc.webservice._native.server.PhoneService;
import cn.zgc.webservice._native.server.PhoneServiceService;
/**
 * 原生webservice,不使用任何框架,使用JDK的api即可实现
 * 客户端，这里无法直接运行，但是代码是正确的 
 * 通过wsdl文件生成客户端所需的jar（这里是phoneService.jar）包步骤：
 * ①、wsimport http://127.0.0.1:9000/phoneService?wsdl
 * ②、jar -cvf phoneService.jar cn
 * @author gczhang
 */
public class PhoneClient {
	public static void main(String[] args) {
		PhoneServiceService pss = new PhoneServiceService();
		PhoneService ps = pss.getPhoneServicePort();
		Phone p = ps.getPhoneInfo("ios");
		System.out.println(p.getTotal());
	}
}
