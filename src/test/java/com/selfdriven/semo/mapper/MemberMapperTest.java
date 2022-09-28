package com.selfdriven.semo.mapper;

import com.selfdriven.semo.dto.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void 멤버생성테스트() {
        memberMapper.deleteAllMember();
        Member member = new Member("tester1", "tester1@gmail.com", "테스터1", "01011112222");
        memberMapper.insertMember(member);
//        assertTrue(memberMapper.countAllMember() == 1);
        assertThat(memberMapper.countAllMember()).isEqualTo(1);
    }

    @Test
    public void 멤버조회테스트(){
        memberMapper.deleteAllMember();
        Member member = new Member("tester1", "tester1@gmail.com", "테스터1", "01011112222");
        memberMapper.insertMember(member);
        Member foundMember = memberMapper.selectMember("tester1");
        assertThat(foundMember.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    public void 전체멤버조회테스트() {
        memberMapper.deleteAllMember();
        int num = 6;
        for (int i = 1; i < num; i++) {
            Member member = new Member("tester" + i, "tester" + i + "@gmail.com", "테스터" + i, "010" + i*11111111);
            memberMapper.insertMember(member);
        }
        List<Member> memberList = memberMapper.getMemberList();
        assertThat(memberList.size()).isEqualTo(num - 1);
    }

    @Test
    public void 전체멤버삭제테스트() {
        memberMapper.deleteAllMember();
        assertThat(memberMapper.countAllMember()).isEqualTo(0);
    }

    @Test
    public void 멤버카운트테스트() {
        memberMapper.deleteAllMember();
        for (int i = 1; i < 6; i++) {
            Member member = new Member("tester" + i, "tester" + i + "@gmail.com", "테스터" + i, "010" + i*11111111);
            memberMapper.insertMember(member);
        }
        assertTrue(memberMapper.countAllMember() == 5);
    }

}