package com.wanted.preonboarding.member;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class memberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 정상 저장 확인")
    public void saveMember(){
        //given
        final Member member = Member.builder()
                .memberIdx(1L)
                .email("user@gmail.com")
                .password("password")
                .build();

        //when
        final Member result = memberRepository.save(member);

        //then
        assertThat(result.getMemberIdx()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@gmail.com");
        assertThat(result.getPassword()).isEqualTo("password");
    }
}
