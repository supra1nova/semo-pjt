package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.entity.Member;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse joinMember(@Valid @RequestBody Member member, HttpSession session) {
        Login login = memberService.insertMember(member);
        if(login != null){
            session.setAttribute("login", login);
        }
        return ApiResponse.ok(login);
    }

    // TODO: Admin 페이지가 구현되면 memberList 를 연결해주자
    @GetMapping("/list")
    public ApiResponse getMemberList() {
        List<Member> result = memberService.getMemberList();
        return ApiResponse.ok(result);
    }

    // 회원 정보 조회 - 본인만 되도록
    @GetMapping("/info")
    public ApiResponse getMemberByEmail(String email) {
        Member result = memberService.getMemberByEmail(email);
        return ApiResponse.ok(result);
    }

    @PostMapping("/edit")
    public ApiResponse editMember(@Valid @RequestBody Member member, HttpSession session) {
        Login login = memberService.updateMember(member);
        if(login != null){
            session.setAttribute("login", login);
        }
        return ApiResponse.ok(login);
    }

    @PostMapping("/withdraw")
    public ApiResponse withdrawMember(HttpSession session) {
        Login login = (Login) session.getAttribute("login");
        int result = memberService.deleteMember(login.getId());
        return ApiResponse.ok(result);
    }

//    @PostMapping("/phNum")
//    public ApiResponse phNumMember(HttpSession session) {
//        Login login = (Login) session.getAttribute("login");
//        String phNum = memberService.getPhNumByMemberId(login.getId());
//        return ApiResponse.ok(phNum);
//    }
}
