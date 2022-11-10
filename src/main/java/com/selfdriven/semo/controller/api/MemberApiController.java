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
        ApiResponse response = null;
        if(login != null){
            session.setAttribute("login", login);
            response = ApiResponse.ok(login);
            return response;
        }
        response = ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @GetMapping("/list")
    public ApiResponse getMemberList() {
        List<Member> result = memberService.getMemberList();
        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @GetMapping("/info")
    public ApiResponse getMemberByEmail(String email) {
        Member result = memberService.getMemberByEmail(email);
        ApiResponse response = result != null ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/edit")
    public ApiResponse editMember(Member member) {
        int result = memberService.updateMember(member);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/withdraw")
    public ApiResponse withdrawMember(@RequestParam String memberId) {
        int result = memberService.deleteMember(memberId);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }
}
