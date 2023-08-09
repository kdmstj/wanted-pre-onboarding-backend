package com.wanted.preonboarding.post;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.repository.MemberRepository;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class postRepositoryTest {

    @AfterEach
    public void afterEach(){
        postRepository.deleteAll();
    }

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("게시물 저장")
    public void savePost(){
        //given
        Member member = memberRepository.save(member(1L));
        Post post = post(1L);
        post.setMember(member);

        //when
        final Post result = postRepository.save(post);

        //then
        assertThat(result.getPostIdx()).isNotNull();
        assertThat(result.getContent()).isEqualTo("게시물 등록 테스트"+1L);
    }

    @Test
    @DisplayName("게시물 목록 조회")
    public void getPostList(){
        //given
        Member member = memberRepository.save(member(1L));
        for(int i = 0; i < 2; i++){
            Post post = post((long) i);
            post.setMember(member);
            postRepository.save(post);
        }

        //when
        Page<Post> result1 = postRepository.findAll(PageRequest.of(0, 10));
        //then
        assertThat(result1.getTotalPages()).isEqualTo(1);
        assertThat(result1.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시물 특정 아이디로 조회 - 정상 조회")
    public void getPost(){
        //given
        Long postIdx = 1L;
        Post post = post(postIdx);
        postRepository.save(post);

        //when
        Optional<Post> optional = postRepository.findById(postIdx);

        //then
        assertThat(optional).isNotNull();
    }

    @Test
    @DisplayName("게시물 특정 아이디로 조회 - 존재하지 않는 게시물")
    public void getPost2(){
        //given
        Long postIdx = 1L;

        //when
        Optional<Post> optional = postRepository.findById(postIdx);

        //then
        assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("게시물 수정")
    public void patchPost(){
        //given
        Long postIdx = 1L;
        Post post = post(postIdx);
        Post savedPost = postRepository.save(post);

        //when
        savedPost.setContent("수정한 게시물");
        Post result = postRepository.save(savedPost);

        //then
        assertThat(result.getContent()).isEqualTo("수정한 게시물");
    }

    @Test
    @DisplayName("게시물 삭제")
    public void deletePost(){
        //given
        Long postIdx = 1L;
        Post post = post(postIdx);
        Post savedPost = postRepository.save(post);

        //when
        postRepository.delete(savedPost);

        //then
        Optional<Post> optional = postRepository.findById(1L);
        assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("게시물 작성자 조회 - 정상 조회")
    public void verifyWriter(){
        //given
        Long memberIdx = 1L;
        Member member = memberRepository.save(member(memberIdx));
        Long postIdx = 1L;
        Post post = post(postIdx);
        post.setMember(member);
        postRepository.save(post);

        //when
        Optional<Post> optional = postRepository.findPostByPostIdxAndMember_MemberIdx(postIdx, memberIdx);

        //then
        assertThat(optional).isNotEmpty();
    }

    @Test
    @DisplayName("게시물 작성자 조회 - 존재하지 않는 게시글")
    public void verifyWriter2(){
        //given
        Long memberIdx = 1L;
        Member member = memberRepository.save(member(memberIdx));
        Long postIdx = 1L;
        Post post = post(postIdx);
        post.setMember(member);
        postRepository.save(post);

        //when
        Optional<Post> optional = postRepository.findPostByPostIdxAndMember_MemberIdx(postIdx, 2L);

        //then
        assertThat(optional).isEmpty();
    }

    @Test

    private Member member(Long num) {
        return Member.builder()
                .memberIdx(num)
                .email("user"+ num +"@gmail.com")
                .password("password")
                .build();
    }

    private Post post(Long num){
        return Post.builder()
                .postIdx(num)
                .content("게시물 등록 테스트" + num)
                .build();
    }
}
