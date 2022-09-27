package com.selfdriven.semo.mapper;

import com.selfdriven.semo.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberMapper {
    int insertMember(Member member);
    List<Member> getMemberList();
    Member selectMember(String memberId);
    int updateMember(Member member);
    int deleteMember(String memberId);
    int deleteAllMember();
    int countAllMember();
}

