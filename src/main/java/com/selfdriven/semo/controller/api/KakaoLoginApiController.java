package com.selfdriven.semo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfdriven.semo.dto.*;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.AccessDeniedException;
import java.security.SecureRandom;

import static com.selfdriven.semo.enums.LoginEnum.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login/kakao")
public class KakaoLoginApiController {

    private final MemberService memberService;

    @GetMapping("/auth-url")
    public ApiResponse getKakaoAuthorizeUrl() throws UnsupportedEncodingException {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(200, random).toString();

        String redirectURI = URLEncoder.encode(KAKAO_LOGIN_REDIRECT.getString(), "UTF-8");

        StringBuffer apiURL = new StringBuffer();
        apiURL.append("https://kauth.kakao.com/oauth/authorize?");
        apiURL.append("client_id=");
        apiURL.append(KAKAO_REST_API_KEY.getString());
        apiURL.append("&response_type=code");
        apiURL.append("&redirect_uri=");
        apiURL.append(redirectURI);
        apiURL.append("&state=" + state);

        ApiResponse response = ApiResponse.ok(apiURL.toString());
        return response;
    }

    @GetMapping("/tokens")
    public ApiResponse getKakaoAccessToken(@RequestParam String code, HttpSession session) throws UnsupportedEncodingException {
        String reqURL = "https://kauth.kakao.com/oauth/token";
        String redirectURI = URLEncoder.encode(KAKAO_LOGIN_REDIRECT.getString(), "UTF-8");

        ApiResponse response;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + KAKAO_REST_API_KEY.getString());
            sb.append("&redirect_uri=" + redirectURI);
            sb.append("&client_secret=" + KAKAO_CLIENT_SECRET.getString());
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            if(responseCode != 200) throw new Exception("카카오로 부터 토큰을 가져올 수 없습니다.");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = br.readLine())!=null) {
                result += line;
            }

            ObjectMapper mapper = new ObjectMapper();
            KakaoAuthResponse authResponse = mapper.readValue(result, KakaoAuthResponse.class);

            if(authResponse.getAccessToken() == null){
                throw new AccessDeniedException("잘못된 접근입니다.");
            }

            session.setAttribute("accessToken", authResponse.getAccessToken());
            session.setAttribute("refreshToken", authResponse.getRefreshToken());

            br.close();
            bw.close();

            response = ApiResponse.ok(authResponse);

        }catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        }

        return response;
    }

    @GetMapping("/user-info")
    public ApiResponse getUserInfoFromKakao(String accessTk) {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        ApiResponse response;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessTk);
            int responseCode = conn.getResponseCode();

            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 에러 발생
                throw new AccessDeniedException("잘못된 접근입니다.");
            }

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            ObjectMapper mapper = new ObjectMapper();
            KakaoUserInfoResponse userInfoResponse = mapper.readValue(result, KakaoUserInfoResponse.class);

            Member member = memberService.getMemberByEmail(userInfoResponse.getKakaoAccount().getEmail());

            if (member == null) {
                System.out.println("exception 발생");
                throw new Exception();
            }

            Login loginMemberInfo = Login.builder()
                    .email(member.getEmail())
                    .memberType(member.getMemberType())
                    .name(member.getName())
                    .loginType("naver").build();

            response = ApiResponse.ok(loginMemberInfo);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/logout")
    public ApiResponse logout(@RequestParam String accessTk, HttpSession session) {
        String reqUrl;

        reqUrl = "https://kapi.kakao.com/v1/user/logout";

        ApiResponse response;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessTk);
            int responseCode = conn.getResponseCode();

            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 에러 발생
                throw new AccessDeniedException("잘못된 접근입니다.");
            }

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            response = ApiResponse.ok(result);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        }

        session.invalidate();

        return response;
    }

    @GetMapping("/invalide-session")
    public ApiResponse deleteToken(HttpSession session) {
        StringBuilder reqURL = new StringBuilder("https://kauth.kakao.com/oauth/logout");

        reqURL.append("client_id=" + KAKAO_REST_API_KEY.getString());
        reqURL.append("logout_redirect_uri=" + KAKAO_LOGOUT_REDIRECT.getString());

        ApiResponse response;

        try {
            URL url = new URL(reqURL.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            BufferedReader br;

            System.out.println("responseCode = " + responseCode);

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 에러 발생
                throw new AccessDeniedException("잘못된 접근입니다.");
            }

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            response = ApiResponse.ok(result);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        }

        session.invalidate();

        return response;
    }


}
