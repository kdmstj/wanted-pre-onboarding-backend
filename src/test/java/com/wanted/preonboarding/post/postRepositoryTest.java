package com.wanted.preonboarding.post;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class postRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("게시물 정상 저장 확인")
    public void savePost(){
        //given
        final Member member = Member.builder()
                .memberIdx(1L)
                .email("user@gmail.com")
                .password("password")
                .build();
        memberRepository.save(member);

        final Post post = Post.builder()
                .postIdx(1L)
                .content("게시물 등록 테스트")
                .build();

        post.setMember(member);
        //when

        final Post result = postRepository.save(post);

        //then
        assertThat(result.getPostIdx()).isNotNull();
        assertThat(result.getContent()).isEqualTo("게시물 등록 테스트");
    }
}
