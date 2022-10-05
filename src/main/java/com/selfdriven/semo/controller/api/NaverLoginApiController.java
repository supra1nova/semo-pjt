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
@RequestMapping("/api/login/naver")
public class NaverLoginApiController {

    private final MemberService memberService;

    @GetMapping("/auth-url")
    public ApiResponse getNaverAuthorizeUrl() throws UnsupportedEncodingException {
        String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL.getString(), "UTF-8");

        SecureRandom random = new SecureRandom();
        String state = new BigInteger(200, random).toString();

        StringBuffer apiURL = new StringBuffer();
        apiURL.append("https://nid.naver.com/oauth2.0/authorize?");
        apiURL.append("client_id=");
        apiURL.append(NAVER_CLIENT_ID.getString());
        apiURL.append("&response_type=code");
        apiURL.append("&redirect_uri=");
        apiURL.append(redirectURI);
        apiURL.append("&state=" + state);

        ApiResponse response = ApiResponse.ok(apiURL.toString());
        return response;
    }

    @GetMapping("/tokens")
    public ApiResponse getNaverAccessToken(@RequestParam String code, @RequestParam String state, HttpSession session) throws UnsupportedEncodingException {
        String reqURL = "https://nid.naver.com/oauth2.0/token";
        String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL.getString(), "UTF-8");

        ApiResponse response;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + NAVER_CLIENT_ID.getString());
            sb.append("&client_secret=" + NAVER_CLI_SECRET.getString());
            sb.append("&redirect_uri=" + redirectURI);
            sb.append("&code=" + code);
            sb.append("&state=" + state);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) throw new Exception("네이버로 부터 토큰을 가져올 수 없습니다.");
            System.out.println("LoginApiController.getNaverAccessToken - response code = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = br.readLine())!=null) {
                result += line;
            }

            ObjectMapper mapper = new ObjectMapper();
            NaverAuthResponse authResponse = mapper.readValue(result, NaverAuthResponse.class);

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
    public ApiResponse getUserInfoFromNaver(String accessTk) {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

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
            NaverUserInfoResponse userInfoResponse = mapper.readValue(result, NaverUserInfoResponse.class);

            Member member = memberService.getMemberByEmail(userInfoResponse.getResponse().getEmail());
            if (member == null) {
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
    public ApiResponse logout(HttpSession session) {
        session.invalidate();
        ApiResponse response = ApiResponse.ok("토큰이 성공적으로 삭제되었습니다.");
        return response;
    }

    @GetMapping("/invalide-session")
    public ApiResponse deleteToken(@RequestParam String accessTk, HttpSession session) {
        String reqUrl;
        reqUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&";
        reqUrl += "client_id=" + NAVER_CLIENT_ID.getString();
        reqUrl += "&client_secret=" + NAVER_CLI_SECRET.getString();
        reqUrl += "&access_token=" + accessTk;
        reqUrl += "&service_provider=NAVER";

        ApiResponse response;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + reqUrl);
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
            NaverTokenRemoveResponse tokenRemoveResponse = mapper.readValue(result, NaverTokenRemoveResponse.class);

            response = ApiResponse.ok(tokenRemoveResponse);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        }

        session.invalidate();

        return response;
    }


}
