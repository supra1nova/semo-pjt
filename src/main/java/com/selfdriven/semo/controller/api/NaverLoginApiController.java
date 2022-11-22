package com.selfdriven.semo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfdriven.semo.dto.*;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.dto.login.NaverAuthResponse;
import com.selfdriven.semo.dto.login.NaverTokenRemoveResponse;
import com.selfdriven.semo.dto.login.NaverUserInfoResponse;
import com.selfdriven.semo.entity.Member;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.AccessDeniedException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import static com.selfdriven.semo.enums.LoginEnum.*;
import static com.selfdriven.semo.enums.ResultCode.UNREGISTERED_MEMBER;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login/naver")
public class NaverLoginApiController {

    private final MemberService memberService;

    @GetMapping("/auth-url")
    public ApiResponse getNaverAuthorizeUrl() throws UnsupportedEncodingException {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(200, random).toString();

        String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL.getString(), "UTF-8");

        // SSL 인증서 오류 해소
        sslTrustAllCerts();

        StringBuffer apiURL = new StringBuffer();
        apiURL.append("https://nid.naver.com/oauth2.0/authorize?");
        apiURL.append("client_id=");
        apiURL.append(NAVER_CLIENT_ID.getString());
        apiURL.append("&response_type=code");
        apiURL.append("&redirect_uri=");
        apiURL.append(redirectURI);
        apiURL.append("&state=" + state);

        ApiResponse response = ApiResponse.ok(apiURL.toString());

        apiURL.delete(0, apiURL.length());  // StringBuffer 초기화

        return response;
    }


    @GetMapping("/tokens-and-user-info")
    public ApiResponse getNaverAccessToken(@RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {
        String reqURL = "https://nid.naver.com/oauth2.0/token";
        String redirectURI = URLEncoder.encode(NAVER_CALLBACK_URL.getString(), "UTF-8");

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
            sb.append("&client_id=" + NAVER_CLIENT_ID.getString());
            sb.append("&client_secret=" + NAVER_CLI_SECRET.getString());
            sb.append("&redirect_uri=" + redirectURI);
            sb.append("&code=" + code);
            sb.append("&state=" + state);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) throw new Exception("네이버로 부터 토큰을 가져올 수 없습니다.");
            System.out.println(responseCode);

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

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

            response = getUserInfoFromNaver(authResponse.getAccessToken(), session);
            System.out.println("api 내부 : " + response);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
            e.printStackTrace();
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
            e.printStackTrace();
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
    public ApiResponse getUserInfoFromNaver(String accessToken, HttpSession session) throws IOException {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

        ApiResponse response;
        HttpURLConnection conn = null;
        BufferedReader br = null;
        if(accessToken == null){
            accessToken = (String)session.getAttribute("accessToken");
        }
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
            System.out.println("responseCode = " + responseCode);

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            ObjectMapper mapper = new ObjectMapper();
            NaverUserInfoResponse userInfoResponse = mapper.readValue(result, NaverUserInfoResponse.class);
            System.out.println("userInfoResponse.getResponse().getId() = " + userInfoResponse.getResponse().getId());
            System.out.println("userInfoResponse.getResponse().getEmail() = " + userInfoResponse.getResponse().getEmail());

            Login.LoginBuilder loginInfoBuilder = Login.builder()
                    .id(userInfoResponse.getResponse().getId())
                    .email(userInfoResponse.getResponse().getEmail())
                    .socialType("n");

            Member member = memberService.getMemberById(userInfoResponse.getResponse().getId());
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
    public ApiResponse logout(HttpSession session) {
        session.setAttribute("login", null);
        ApiResponse response = ApiResponse.ok("토큰이 성공적으로 삭제되었습니다.");
        return response;
    }

    @GetMapping("/invalid-session")
    public ApiResponse deleteToken(HttpSession session) {
        String accessToken = (String)session.getAttribute("accessToken");
        StringBuilder reqUrl = new StringBuilder();
        reqUrl.append("https://nid.naver.com/oauth2.0/token?grant_type=delete&");
        reqUrl.append("client_id=" + NAVER_CLIENT_ID.getString());
        reqUrl.append("&client_secret=" + NAVER_CLI_SECRET.getString());
        reqUrl.append("&access_token=" + accessToken);
        reqUrl.append("&service_provider=NAVER");

        ApiResponse response;

        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(reqUrl.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + reqUrl);
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
            NaverTokenRemoveResponse tokenRemoveResponse = mapper.readValue(result, NaverTokenRemoveResponse.class);

            response = ApiResponse.ok(tokenRemoveResponse);

        } catch (AccessDeniedException e) {
            response = ApiResponse.fail(ResultCode.ACCESS_DENIED);
        } catch (Exception e) {
            response = ApiResponse.fail(1002, e.getMessage());
        } finally {
            try {
                if (br != null) { //NullPointerException을 방지
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        session.invalidate();
        return response;
    }


    public void sslTrustAllCerts(){
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        SSLContext sc;

        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
