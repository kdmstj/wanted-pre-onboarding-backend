package com.wanted.preonboarding.security.service;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import com.wanted.preonboarding.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);

        Member findMember = optionalMember.orElseThrow(() -> new RuntimeException("MEMBER_NOT_FOUND"));

        System.out.println("member username : " + findMember.getEmail());
        System.out.println("member password : " + findMember.getPassword());

        return new CustomUserDetails(findMember);
    }
}
