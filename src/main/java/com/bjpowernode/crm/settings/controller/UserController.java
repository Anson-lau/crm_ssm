package com.bjpowernode.crm.settings.controller;


import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class UserController {

    @Resource
    private UserService userService;



     /**
      * @Author anson
      * @Description: 驗證登錄信息
      * @Date:  2021/9/8 15:23
      * @Param: loginAct, loginPwd, request
      * @return: Map<String, Object>
      **/
    @RequestMapping("/user/login.do")
    @ResponseBody
    public Map<String, Object> login(String loginAct, String loginPwd, HttpServletRequest request){

        System.out.println("coming into login..");
        System.out.println(loginAct+"==========>"+loginPwd);

        loginPwd = MD5Util.getMD5(loginPwd);
        System.out.println(loginPwd);
        String ip = request.getRemoteAddr();
        System.out.println("ip: "+ip);

        Map<String, Object> map = new HashMap<>();

        try{
            User user = userService.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user", user);

            map.put("success", true);
        }catch(Exception e){

            e.printStackTrace();

            String msg = e.getMessage();

            map.put("success", false);
            map.put("msg", msg);

        }

        return map;

    }



}
