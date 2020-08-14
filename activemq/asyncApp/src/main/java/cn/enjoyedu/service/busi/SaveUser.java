package cn.enjoyedu.service.busi;

import cn.enjoyedu.vo.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 类说明：保存用户的注册信息
 */
@Service
public class SaveUser {

    //用一个Map作为内存数据库，保存用户注册的信息
    private ConcurrentHashMap<String, User> userData =
            new ConcurrentHashMap<String, User>();

    public void saveUser(User user) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userData.putIfAbsent(user.getName(), user);
    }

    public User getUser(String userId) {
        return userData.get(userId);
    }


}
