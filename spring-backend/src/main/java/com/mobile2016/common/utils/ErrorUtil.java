package com.mobile2016.common.utils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

public class ErrorUtil {

    public  static  String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession()
                .getAttribute(key);
        String error ;
        if (exception instanceof BadCredentialsException) {
            error = "无效的用户名或者密码!";
        } else if (exception instanceof UsernameNotFoundException) {
            error = "用户不存在";
        } else if (exception instanceof LockedException) {
            error = "用户被锁定";
        } else {
            error = "系统发生错误!";
        }

        return error;
    }

}
