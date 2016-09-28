package cn.zgc.hessian.client;

import java.net.MalformedURLException;


import com.caucho.hessian.client.HessianProxyFactory;

import cn.zgc.hessian.model.User;
import cn.zgc.hessian.service.IService;

/**
 * <p>ClassName: HessianClient<p>
 * <p>Description: 调用Hessian的客户端<p>
 * @author xudp
 * @version 1.0 V
 * @createTime 2014-8-7 下午07:05:42
 */
public class HessianClient {

    public static void main(String[] args) throws MalformedURLException {
        /* 
            <servlet>
                <!-- 配置 HessianServlet，Servlet的名字随便配置，例如这里配置成ServiceServlet-->
                <servlet-name>ServiceServlet</servlet-name>
                <servlet-class>com.caucho.hessian.server.HessianServlet</servlet-class>
                
                <!-- 配置接口的具体实现类 -->
                <init-param>
                    <param-name>service-class</param-name>
                    <param-value>gacl.hessian.service.impl.ServiceImpl</param-value>
                </init-param>
            </servlet>
            <!-- 映射 HessianServlet的访问URL地址-->
            <servlet-mapping>
                <servlet-name>ServiceServlet</servlet-name>
                <url-pattern>/ServiceServlet</url-pattern>
            </servlet-mapping>
         */
        //在服务器端的web.xml文件中配置的HessianServlet映射的访问URL地址
        String url = "http://127.0.0.1:8080/rpc_study/ServiceServlet";
        HessianProxyFactory factory = new HessianProxyFactory();
        //如果服务端和客户端不在同一处，服务端的接口应该打成jar供客户端使用
        IService service = (IService) factory.create(IService.class, url);//创建IService接口的实例对象
        User user = service.getUser();//调用Hessian服务器端的ServiceImpl类中的getUser方法来获取一个User对象
        System.out.println(user.getName());
    }
}