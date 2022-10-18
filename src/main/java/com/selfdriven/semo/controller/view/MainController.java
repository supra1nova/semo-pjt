package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.dto.login.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class MainController {

    @GetMapping
    public String main(HttpSession session){
        Login loginObject = (Login) session.getAttribute("login");
        if( loginObject != null && loginObject.getMemberType().equals("u")){
            session.setAttribute("login", null);
            session.removeAttribute("login");
        }
        return "main.html";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }
}
