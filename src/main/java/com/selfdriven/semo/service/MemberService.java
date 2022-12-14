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

    public Login updateMember(Member member) {
        int res = memberMapper.updateMember(member);
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
        return login;
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

    // ?????? ?????? ?????? ?????????
    public Boolean checkMemberType(String type, String memberId){
        String memberType = getMembertype(memberId);
        Boolean res = memberType.equals(type);
        return res;
    }

    // ?????? ?????? ?????? ?????????
    public String getMembertype(String memberId){
        return memberMapper.selectMemberType(memberId);
    }

    public String getPhNumByMemberId(String memberId) {
        Member foundMember = getMemberById(memberId);
        return foundMember.getPhNum();
    }
}
