package com.wanted.preonboarding.post;

import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.repository.PostRepository;
import com.wanted.preonboarding.domain.post.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class postServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시물 저장")
    public void savePost() {
        // given
        doReturn(post(1L)).when(postRepository).save(any(Post.class));

        // when
        Post result = postService.addPost(post(1L));

        // then
        assertThat(result.getPostIdx()).isNotNull();
        assertThat(result.getContent()).isEqualTo("생성된 게시물 "+ 1L);
    }

    @Test
    @DisplayName("게시물 목록 조회")
    public void getPostList() {
        //given
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            postList.add(post((long) i));
        }
        Page<Post> postPage = new PageImpl<>(postList);
        doReturn(postPage).when(postRepository).findAll(PageRequest.of(0, 10));
        //when
        Page<Post> result = postService.findPostList(1);

        //then
        assertThat(result.getContent().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("게시물 조회 - 정상 조회")
    public void getPost1() {
        //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post(1L)));

        //when
        Post result = postService.findPost(1L);

        //then
        assertThat(result.getPostIdx()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시물 조회 - 존재하지 않는 게시물 조회")
    public void getPost2() {
        //given
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when - then
        Assertions.assertThrows(RuntimeException.class, () -> postService.findPost(2L));
    }

    @Test
    @DisplayName("게시물 권한 확인 - 권한 있음")
    public void verifyWriter() {
        //given
        when(postRepository.findPostByPostIdxAndMember_MemberIdx(1L, 1L)).thenReturn(Optional.of(post(1L)));

        //when
        Post verifiedPost = postService.verifyWriter(1L, 1L);
        //then
        assertThat(verifiedPost.getPostIdx()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시물 권한 확인 - 권한 없음")
    public void verifyWriter2() {
        //given
        when(postRepository.findPostByPostIdxAndMember_MemberIdx(1L, 2L)).thenReturn(Optional.empty());

        //when - then
        Assertions.assertThrows(RuntimeException.class, () -> postService.verifyWriter(1L, 2L));
    }


    private Member member() {
        String encryptedPassword = passwordEncoder.encode("password");
        return Member.builder()
                .memberIdx(1L)
                .email("user@gmail.com")
                .password(encryptedPassword)
                .build();
    }

    private Post post(Long postIdx) {
        return Post.builder()
                .postIdx(postIdx)
                .content("생성된 게시물 " + postIdx)
                .build();
    }
}




