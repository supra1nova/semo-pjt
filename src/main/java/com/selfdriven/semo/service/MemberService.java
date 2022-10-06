package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.Member;
import com.selfdriven.semo.mapper.MemberMapper;
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

    public int insertMember(Member member) {
        return memberMapper.insertMember(member);
    }

    public List<Member> getMemberList() {
        return memberMapper.getMemberList();
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
}
