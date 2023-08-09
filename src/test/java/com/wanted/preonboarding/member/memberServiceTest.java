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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class memberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 저장(패스워드 암호화)")
    public void addMember() {
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

    @Test
    @DisplayName("회원 조회(정상)")
    public void findMember1(){
        //given
        Long memberIdx = 1L;
        Member member = Member.builder().memberIdx(memberIdx).email("user@gmail.com").password("password").build();
        given(memberRepository.findById(memberIdx)).willReturn(java.util.Optional.ofNullable(member));
        //when
        Member findMember = memberService.findMember(memberIdx);
        //then
        assertThat(findMember.getEmail()).isEqualTo("user@gmail.com");
    }

    @Test
    @DisplayName("회원 조회(조회 하는 회원이 없는 경우)")
    public void findMember2(){
        //given
        Long memberIdx = 1L;
        Member member = Member.builder().memberIdx(memberIdx).email("user@gmail.com").password("password").build();
        given(memberRepository.findById(2L)).willThrow(new RuntimeException("MEMBER_NOT_FOUND"));
        //when
        Member findMember = memberService.findMember(memberIdx);
        //then
        assertThat(findMember.getEmail()).isNull();
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


