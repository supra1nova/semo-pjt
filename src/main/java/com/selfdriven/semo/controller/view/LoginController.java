package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.controller.api.KakaoLoginApiController;
import com.selfdriven.semo.controller.api.NaverLoginApiController;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    public MemberService memberService;

    public final NaverLoginApiController naverLoginApiController;
    public final KakaoLoginApiController kakaoLoginApiController;

    // 1. 로그인 페이지로 return
    @GetMapping
    public String login(Model model) {
        model.addAttribute("authRequestUrl", "/semo/login");
        return "login";
    }

    // 2. 네이버 로그인 인증 요청 버튼에 필요한 URL 정보 생성 후 리다이렉. state에 들어갈 내용도 생성.
    @RequestMapping("/naver")
    public String naverLogin(HttpSession session, Model model) throws UnsupportedEncodingException {
        model.addAttribute("naverApiURL", naverLoginApiController.getNaverAuthorizeUrl());
        return "redirect:" + naverLoginApiController.getNaverAuthorizeUrl();
    }

    // 3. 카카오 로그인 인증 요청 버튼에 필요한 URL 정보 생성 후 리다이렉. state에 들어갈 내용도 생성.
    @RequestMapping("/kakao")
    public String kakaoLogin(HttpSession session, Model model) throws UnsupportedEncodingException {
        model.addAttribute("kakaoApiURL", kakaoLoginApiController.getKakaoAuthorizeUrl());
        return "redirect:" + kakaoLoginApiController.getKakaoAuthorizeUrl();
    }

//    // 3. 인증 요청 redirect 후 서비스 내 유저 정보 확인
//    @RequestMapping("/naver-callback")
//    public String login(@RequestParam String code, @RequestParam String state, HttpSession session) throws UnsupportedEncodingException {
//        ModelAndView mav = new ModelAndView();
//        // 1. 네이버 로그인 인증 요청
//        Login loginTokenDto = loginApiController.getNaverAccessToken(code, state);
//
//        if(loginTokenDto != null && !loginTokenDto.equals("")) {
//
//            String accessTk = loginTokenDto.getAccessTk();
//            String refreshTk = loginTokenDto.getRefreshTk();
//
//            mav.addObject("accessTk", accessTk);
//            mav.addObject("refreshTk", refreshTk);
//
//            // 2번 유저 정보 요청
//            Login loginEmailInfoDto = loginApiController.getUserInfoFromNaver(accessTk);
//
//            if(loginEmailInfoDto != null && !loginTokenDto.equals("") && loginEmailInfoDto.getEmail() != null && !loginEmailInfoDto.getEmail().equals("")) {
//                String email = loginEmailInfoDto.getEmail();
//                Member member = loginApiController.getMemberFromDB(email);
//
//                Gson gson = new Gson();
//                String json = gson.toJson(member);
//                System.out.println("json = " + json);
//
//                ApiResponse response = member != null ? ApiResponse.ok(member) : ApiResponse.fail(1002, "서비스에 가입된 유저가 존재하지 않습니다.");
//                mav.addObject("response", response);
//                session.setAttribute("response", response);
//                session.setAttribute("member", member);
//                session.setAttribute("data", response.getData().toString());
//                session.setAttribute("email", email);
//            } else {
//                return "login";
//            }
//
//
//            mav.setViewName("naver-callback");
//            session.setAttribute("accessTk", accessTk);
//            session.setAttribute("refreshTk", refreshTk);
//        }else {
//            ApiResponse response = ApiResponse.fail(1002, "카카오톡에 가입된 유저가 존재하지 않습니다.");
//            mav.addObject("response", response);
//        }
////        return "forward:http://localhost/semo/";
//        return "main";
////        return mav;
//    }

}
