package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.dto.login.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
@RequestMapping
public class MainController {

    @GetMapping
    public String main(HttpSession session, HttpServletRequest req){
        Login loginObject = (Login) session.getAttribute("login");
        if( loginObject != null && loginObject.getMemberType().equals("u")){
            session.setAttribute("login", null);
            session.removeAttribute("login");
        }
        return "main";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }
}
