package cn.zgc.hessian.service;

import cn.zgc.hessian.model.User;

/**
 *  IService接口的具体实现类
 */
public class ServiceImpl implements IService {

    /* (non-Javadoc)
     * @MethodName getUser
     * @Description 实现IService接口的getUser方法
     * @author xudp
     * @return 返回一个User对象
     * @see gacl.hessian.service.IService#getUser()
     */
    public User getUser() {
        return new User("小生怕怕");
    }
}