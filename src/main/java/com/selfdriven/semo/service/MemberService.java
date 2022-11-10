package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Member;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    // DI 의존성 주입 - 생성자 메서드 주입 방식
//    @Autowired
//    MemberMapper memberMapper;
    private final MemberMapper memberMapper;

    public Login insertMember(Member member) {
        int res = memberMapper.insertMember(member);
        Login.LoginBuilder loginBuilder = Login.builder();
        if(res != 0){
            loginBuilder
                .id(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .socialType(member.getSocialType())
                .build();
        }
        Login login = loginBuilder.build();
        System.out.println("login = " + login);
        return login;
    }

    public List<Member> getMemberList() {
        return memberMapper.selectAllMember();
    }

    public Member getMemberByEmail(String email){
        return memberMapper.selectMember(email);
    }

    public Member getMemberById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    public int updateMember(Member member) {
        return memberMapper.updateMember(member);
    }

    public int deleteMember(String memberId){
        return memberMapper.deleteMember(memberId);
    }

    public int deleteAllMember(){
        return memberMapper.deleteAllMember();
    }

    public int countAllMember(){
        return memberMapper.countAllMember();
    }

    // 멤버 타입 확인 메서드
    public Boolean checkMemberType(String type, String memberId){
        String memberType = getMembertype(memberId);
        Boolean res = memberType.equals(type);
        return res;
    }

    // 멤버 타입 조회 메서드
    public String getMembertype(String memberId){
        return memberMapper.selectMemberType(memberId);
    }
}
