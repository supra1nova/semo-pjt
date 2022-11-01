package com.selfdriven.semo.repository;

import com.selfdriven.semo.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberMapper {
    int insertMember(Member member);
    List<Member> selectAllMember();
    Member selectMember(String email);
    Member selectMemberById(String memberId);
    int updateMember(Member member);
    int deleteMember(String memberId);
    int deleteAllMember();
    int countAllMember();
    String selectMemberType(String memberId);
}

