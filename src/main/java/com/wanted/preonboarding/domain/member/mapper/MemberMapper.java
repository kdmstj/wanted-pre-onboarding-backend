package com.wanted.preonboarding.domain.member.mapper;

import com.wanted.preonboarding.domain.member.dto.MemberDto;
import com.wanted.preonboarding.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post memberPostDto);
}
