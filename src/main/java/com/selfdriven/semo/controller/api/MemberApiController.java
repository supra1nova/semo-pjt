package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.Member;
import com.selfdriven.semo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ApiResponse getMemberList() {
        List<Member> result = memberService.getMemberList();
        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @GetMapping("/info")
    public ApiResponse getMember(String memberId) {
        Member result = memberService.getMember(memberId);
        ApiResponse response = result != null ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/join")
    public ApiResponse joinMember(@Valid @RequestBody Member member) {
        int result = memberService.insertMember(member);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/withdraw")
    public ApiResponse withdrawMember(@RequestParam String memberId) {
        int result = memberService.deleteMember(memberId);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/edit")
    public ApiResponse editMember(Member member) {
        int result = memberService.updateMember(member);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }
}
