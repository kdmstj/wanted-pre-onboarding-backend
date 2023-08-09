package com.wanted.preonboarding.member;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import com.wanted.preonboarding.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class memberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 패스워드 암호화 확인")
    public void saveMember() {
        // given
        doReturn(member()).when(memberRepository).save(any(Member.class));

        // when
        Member member = Member.builder()
                .memberIdx(1L)
                .email("user@gmail.com")
                .password("password").build();

        Member result = memberService.addMember(member);

        // then
        assertThat(result.getMemberIdx()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@gmail.com");
        assertThat(result.getPassword()).isNotEqualTo("password");

    }

    private Member member() {
        String encryptedPassword = passwordEncoder.encode("password");
        return Member.builder()
                .memberIdx(1L)
                .email("user@gmail.com")
                .password(encryptedPassword)
                .build();
    }
}


