package cn.enjoyedu.service.reg;


import cn.enjoyedu.vo.User;

/**
 * <p>
 * 类说明：用户注册的接口，因为我们将有多个实现
 */
public interface IUserReg {

    public boolean userRegister(User user);

}
