package com.selfdriven.semo.controller.view;

import com.selfdriven.semo.controller.api.KakaoLoginApiController;
import com.selfdriven.semo.controller.api.NaverLoginApiController;
import com.selfdriven.semo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    public final NaverLoginApiController naverLoginApiController;
    public final KakaoLoginApiController kakaoLoginApiController;

    // 1. 로그인 페이지를 화면에 띄운다.
    @GetMapping
    public String login() {
        return "login";
    }

    // 2. 카카오 로그인 인증 요청, 토큰 발급, 회원 정보 조회 후 메인으로 리턴.
    @GetMapping("/kakao")
    public String kakaoLogin(@RequestParam String code, HttpSession session, RedirectAttributes rdrt) throws IOException {
        ApiResponse response = kakaoLoginApiController.getKakaoAccessToken(code, session);
        return getRedirect(rdrt, response);
    }

    // 3. 네이버 로그인 인증 요청, 토큰 발급, 회원 정보 조회 후 메인으로 리턴.
    @GetMapping("/naver")
    public String naverLogin(@RequestParam String code, @RequestParam String state, HttpSession session, RedirectAttributes rdrt) throws IOException {
        ApiResponse response = naverLoginApiController.getNaverAccessToken(code, state, session);
        return getRedirect(rdrt, response);
    }

    private static String getRedirect(RedirectAttributes rdrt, ApiResponse response) {
        if(response.getResultCode() == 1008) {
            return "redirect:/join";
        }
        rdrt.addFlashAttribute("response", response);
        return "redirect:/";
    }

}
