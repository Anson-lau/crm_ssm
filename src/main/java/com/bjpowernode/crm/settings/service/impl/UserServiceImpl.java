package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("用戶不存在");
        }

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("登錄超時");
        }

        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("賬號被鎖定");
        }

        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip被限制登錄");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }
}
