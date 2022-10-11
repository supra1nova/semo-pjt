package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.controller.api.KakaoLoginApiController;
import com.selfdriven.semo.controller.api.NaverLoginApiController;
import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.enums.LoginEnum;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login-out")
public class LoginController {

    public MemberService memberService;

    public final NaverLoginApiController naverLoginApiController;
    public final KakaoLoginApiController kakaoLoginApiController;

    // 1. 로그인 페이지를 화면에 띄운다.
    @GetMapping
    public String login() {
        return "login";
    }

    // 2. 카카오 로그인 인증 요청, 토큰 발급, 회원 정보 조회 후 메인으로 리턴.
    @GetMapping("/kakao-login")
    public String kakaoLogin(@RequestParam String code, HttpSession session, RedirectAttributes rdrt) throws IOException {
        ApiResponse response = kakaoLoginApiController.getKakaoAccessToken(code, session);
        if(response.getResultCode() == 1008) {  // 서비스 내 로그인 정보 부재시 회원가입 페이지로 이동
            return "join";
        }
        System.out.println("response.getData() = " + response.getData());
        System.out.println("test : " + response.getData().getClass().toString());
        rdrt.addFlashAttribute("response", response);
        return "redirect:/";
    }

    // 3. 네이버 로그인 인증 요청, 토큰 발급, 회원 정보 조회 후 메인으로 리턴.
    @GetMapping("/naver-login")
    public String naverLogin(@RequestParam String code, @RequestParam String state, HttpSession session, RedirectAttributes rdrt) throws IOException {
        ApiResponse response = naverLoginApiController.getNaverAccessToken(code, state, session);
        if(response.getResultCode() == 1008) {
            return "join";
        }
        System.out.println("response.getData() = " + response.getData());
        System.out.println("test : " + response.getData().getClass().toString());
        rdrt.addFlashAttribute("response", response);
        return "redirect:/";
    }

}
