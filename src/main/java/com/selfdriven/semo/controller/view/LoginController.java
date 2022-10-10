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

    // 1. 로그인 페이지로 return
    @GetMapping
    public String login() {
        return "login";
    }

    // 2. 카카오 로그인 인증 요청, 토큰 발급, 회원 정보 조회 후 메인으로 리턴.
    @GetMapping("/kakao-login")
    public String kakaoLogin(@RequestParam String code, HttpSession session, RedirectAttributes rdrt) throws IOException {
        ApiResponse response = kakaoLoginApiController.getKakaoAccessToken(code, session);
        if(response.getResultCode() == 1008) {
            return "join";
        }
        System.out.println("response.getData() = " + response.getData());
        System.out.println("test : " + response.getData().getClass().toString());
        rdrt.addFlashAttribute("response", response);
        return "redirect:/";
    }

    // 3. 네이버 로그인 인증 요청 버튼에 필요한 URL 정보 생성 후 리다이렉. state에 들어갈 내용도 생성.
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
