package cn.zgc.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/* 
* Context接口表示一个命名上下文，它由一组名称到对象的绑定组成。 
* 它包含检查和更新这些绑定的一些方法。 
*/
import javax.naming.Context;
/* 
* InitialContext类是执行命名操作的初始上下文。    
* 该初始上下文实现 Context 接口并提供解析名称的起始点。 
*/
import javax.naming.InitialContext;

public class Server {
	public static void main(String[] args) {
		try {
			/*
	         * Locate registry，您可以理解成RMI服务注册表，或者是RMI服务位置仓库。
	         * 主要的作用是维护一个“可以正常提供RMI具体服务的所在位置”。
	         * 每一个具体的RMI服务提供者，都会将自己的Stub注册到Locate registry中，以表示自己“可以提供服务”
	         * 
	         * 有两种方式可以管理Locate registry，一种是通过操作系统的命令行启动注册表；
	         * 另一种是在代码中使用LocateRegistry类。
	         * 
	         * LocateRegistry类中有一个createRegistry方法，可以在这台物理机上创建一个“本地RMI注册表”
	         * */
			//加上这一句，就不需要命令行注册
			LocateRegistry.createRegistry(1099);
			
			// 实例化实现了IService接口的远程服务ServiceImpl对象
			IService service02 = new ServiceImpl("service02");
			// 初始化命名空间
			/*Context namingContext = new InitialContext();
			// 将名称绑定到对象,即向命名空间注册已经实例化的远程服务对象
			namingContext.rebind("rmi://127.0.0.1/service02", service02);*/
			Naming.rebind("rmi://127.0.0.1:1099/service02", service02);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("服务器向命名表注册了1个远程服务对象！");
	}
}