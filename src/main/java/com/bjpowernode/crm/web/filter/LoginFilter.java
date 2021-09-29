package com.bjpowernode.crm.web.filter;


import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;




public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("coming into loginFilter..");

        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path) || "/login.jsp".equals(path)){
            filterChain.doFilter(req, resp);
        }else{
            HttpSession session = req.getSession();
            User user = (User)session.getAttribute("user");
            if (user != null){
                filterChain.doFilter(req, resp);
            }else{
                resp.sendRedirect(req.getContextPath()+"/login.jsp");
            }
        }


    }

    @Override
    public void destroy() {

    }
}
