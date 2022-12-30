package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.dto.login.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class MainController {

    @GetMapping("/")
    public String main(HttpSession session){
        Login loginObject = (Login) session.getAttribute("login");
        if( loginObject != null && loginObject.getMemberType().equals("u")){
            session.setAttribute("login", null);
            session.removeAttribute("login");
        }
        return "main";
    }

    @GetMapping("/roomdetail")
    public String roomdetail(){
        return "roomDetail";
    }
    
    @GetMapping("/reservation/{productId}")
    public String reservation(@PathVariable String productId, ModelAndView mv){
        mv.addObject("productId", productId);
        mv.setViewName("productInfo");
        return "reservation";
    }

    @GetMapping("/paycheck")
    public String paycheck(){
        return "paycheck";
    }
    @GetMapping("/paycheck/fine")
    public String paycheckFine(){
        return "paycheckFine";
    }

    @GetMapping("/location/{productId}")
    public ModelAndView location(@PathVariable String productId, ModelAndView mv){
        mv.addObject("productId", productId);
        mv.setViewName("location");
        return mv;
    }


    @GetMapping("/mytrip")
    public String mytrip(){
        return "mytrip";
    }

    @GetMapping("/mypage")
    public String myPage(){
        return "mypage";
    }

    @GetMapping("/mybusiness")
    public String myBusiness(){
        return "myBusiness";
    }

    @GetMapping("/mypage/review")
    public String mypageReview(){
        return "mypageReview";
    }
    

    

    @GetMapping("/join")
    public String join(){
        return "join";
    }

//    @GetMapping("/login")
//    public String login(){
//        return "login";
//    }

    @GetMapping("/join/fine")
    public String joinFine(){
        return "joinFine";
    }
    @GetMapping("/uploadtest")
    public String uploadtest(){
        return "uploadtest";
    }

    @GetMapping("/product/info/{productId}")
    public ModelAndView productInfo(@PathVariable String productId, ModelAndView mv){
        mv.addObject("productId", productId);
        mv.setViewName("productInfo");
        return mv;
    }

    @GetMapping("/product/create")
    public String productUpdate(){
        return "productCreate";
    }

    @GetMapping("/product/update/{productId}")
    public ModelAndView productUpdate(@PathVariable String productId, ModelAndView mv){
        mv.addObject("productId", productId);
        mv.setViewName("productUpdate");
        return mv;
    }

    @GetMapping("/product/list")
    public String productList(){
        return "productList";
    }


    @GetMapping("/qna/{productId}")
    public ModelAndView qna(@PathVariable String productId, ModelAndView mv){
        mv.addObject("productId", productId);
        mv.setViewName("qna");
        return mv;
    }
}
