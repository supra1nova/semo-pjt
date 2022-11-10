package com.selfdriven.semo.util;

import com.selfdriven.semo.dto.login.Login;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static Login getLoginFromSession(HttpSession session){
        Login login = (Login)(session.getAttribute("login"));
        return login;
    }
}
