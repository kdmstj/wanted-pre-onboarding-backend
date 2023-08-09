package com.wanted.preonboarding.domain.member.service;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import com.wanted.preonboarding.exception.CustomException;
import com.wanted.preonboarding.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member addMember(Member member){

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);
        return memberRepository.save(member);
    }

    public Member findMember(Long memberIdx) {
        Optional<Member> optional =  memberRepository.findById(memberIdx);

        return optional.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
