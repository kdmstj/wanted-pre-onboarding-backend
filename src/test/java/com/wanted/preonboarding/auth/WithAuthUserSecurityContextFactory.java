package com.wanted.preonboarding.auth;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser annotation) {

        String email = annotation.email();
        Long memberIdx = annotation.memberIdx();
        Member member = new Member(memberIdx, email, "password");
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(customUserDetails, null,null);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
