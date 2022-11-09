package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberMapper {
    Integer insertMember(Member member);
    List<Member> selectAllMember();
    Member selectMember(String email);
    Member selectMemberById(String memberId);
    Integer updateMember(Member member);
    Integer deleteMember(String memberId);
    Integer deleteAllMember();
    Integer countAllMember();
    String selectMemberType(String memberId);
}

