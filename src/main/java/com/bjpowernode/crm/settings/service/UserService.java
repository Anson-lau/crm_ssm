package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws Exception;

    List<User> getUserList();

}
