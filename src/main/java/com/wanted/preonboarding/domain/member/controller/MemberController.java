package com.wanted.preonboarding.domain.member.controller;

import com.wanted.preonboarding.domain.member.dto.MemberDto;
import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.mapper.MemberMapper;
import com.wanted.preonboarding.domain.member.service.MemberService;
import com.wanted.preonboarding.util.UriUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private static final String DEFAULT_URI = "/members";

    @PostMapping
    public ResponseEntity<?> postMember(@Valid @RequestBody MemberDto.Post requestBody){

        Member postMember = memberMapper.memberPostDtoToMember(requestBody);
        Member createdMember = memberService.addMember(postMember);

        URI uri = UriUtil.createUri(DEFAULT_URI, createdMember.getMemberIdx());
        return ResponseEntity.created(uri).build();
    }
}
