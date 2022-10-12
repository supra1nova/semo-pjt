package com.selfdriven.semo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfdriven.semo.dto.*;
import com.selfdriven.semo.dto.login.KakaoAuthResponse;
import com.selfdriven.semo.dto.login.KakaoUserInfoResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
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
import static com.selfdriven.semo.enums.ResultCode.UNREGISTERED_MEMBER;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login-out/kakao-login")
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

        apiURL.delete(0, apiURL.length());  // StringBuffer 초기화

        return response;
    }


    @GetMapping("/tokens-and-user-info")
    public ApiResponse getKakaoAccessToken(@RequestParam String code, HttpSession session) throws IOException {
        String reqURL = "https://kauth.kakao.com/oauth/token";
        String redirectURI = URLEncoder.encode(KAKAO_LOGIN_REDIRECT.getString(), "UTF-8");

        ApiResponse response;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(reqURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + KAKAO_REST_API_KEY.getString());
            sb.append("&redirect_uri=" + redirectURI);
            sb.append("&client_secret=" + KAKAO_CLIENT_SECRET.getString());
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) throw new Exception("카카오로 부터 토큰을 가져올 수 없습니다.");

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

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

            response = getUserInfoFromKakao(authResponse.getAccessToken(), session);
            System.out.println("api 내부 : " + response);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        } finally {
            sb.delete(0, sb.length());  // StringBuffer 초기화
            if (bw != null) { //NullPointerException을 방지
                bw.close();
            }
            if (br != null) { //NullPointerException을 방지
                br.close();
            }
            if (conn != null){ //NullPointerException을 방지
                conn.disconnect();
            }
        }
        return response;
    }


    @GetMapping("/user-info")
    public ApiResponse getUserInfoFromKakao(String accessToken, HttpSession session) throws IOException {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        ApiResponse response;
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();

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
            System.out.println("userInfoResponse.getId() = " + userInfoResponse.getId());
            System.out.println("userInfoResponse.getKakaoAccount().getEmail() = " + userInfoResponse.getKakaoAccount().getEmail());

            Login.LoginBuilder loginInfoBuilder = Login.builder()
                    .id(userInfoResponse.getId().toString())
                    .email(userInfoResponse.getKakaoAccount().getEmail())
                    .socialType("k");

            Member member = memberService.getMemberById(userInfoResponse.getId().toString());
            if (member == null) {
                Login loginMemberInfo = loginInfoBuilder
                        .memberType("u")
                        .build();
                session.setAttribute("login", loginMemberInfo);
                throw new ApiException(UNREGISTERED_MEMBER.getCode(), UNREGISTERED_MEMBER.getMessage());
            }

            Login loginMemberInfo = loginInfoBuilder
                    .name(member.getName())
                    .memberType(member.getMemberType())
                    .build();

            System.out.println("member.getSocialType() = " + member.getSocialType());
            System.out.println("loginMemberInfo = " + loginMemberInfo);

            response = ApiResponse.ok(loginMemberInfo);

            session.setAttribute("login", response.getData());

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (ApiException e) {
            response = ApiResponse.fail(UNREGISTERED_MEMBER.getCode(), UNREGISTERED_MEMBER.getMessage());
        } catch (Exception e) {
            response = ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        } finally {
            if (br != null) { //NullPointerException을 방지
                br.close();
            }
            if (conn != null){ //NullPointerException을 방지
                conn.disconnect();
            }
        }
        return response;
    }

    @GetMapping("/logout")
    public ApiResponse logout(@RequestParam String accessToken, HttpSession session) throws IOException {
        String reqUrl;

        reqUrl = "https://kapi.kakao.com/v1/user/logout";

        ApiResponse response;
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();

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

            session.setAttribute("login", null);

            response = ApiResponse.ok(result);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        } finally {
            if (br != null) { //NullPointerException을 방지
                br.close();
            }
            if (conn != null){ //NullPointerException을 방지
                conn.disconnect();
            }
        }

        return response;
    }

    @GetMapping("/invalid-session")
    public ApiResponse deleteToken(HttpSession session) throws IOException {
        StringBuilder reqUrl = new StringBuilder("https://kauth.kakao.com/oauth/logout");
        reqUrl.append("client_id=" + KAKAO_REST_API_KEY.getString());
        reqUrl.append("logout_redirect_uri=" + KAKAO_LOGOUT_REDIRECT.getString());

        ApiResponse response;

        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(reqUrl.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

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
            session.setAttribute("login", null);

            response = ApiResponse.ok(result);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        } finally {
            if (br != null) { //NullPointerException을 방지
                br.close();
            }
            if (conn != null){ //NullPointerException을 방지
                conn.disconnect();
            }
        }

        session.invalidate();
        return response;
    }

}
