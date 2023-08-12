package com.wanted.preonboarding.domain.post.service;

import com.wanted.preonboarding.domain.post.dto.PostDto;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.repository.PostRepository;
import com.wanted.preonboarding.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.wanted.preonboarding.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static com.wanted.preonboarding.exception.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post addPost(Post post) {

        return postRepository.save(post);
    }

    public Page<Post> findPostList(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, 10);
        return postRepository.findAll(pageable);
    }

    public Post findPost(Long postIdx) {
        Optional<Post> optional = postRepository.findById(postIdx);
        return optional.orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    public Post patchPost(Long postIdx, PostDto.Patch patchDto, Long memberIdx) {
        Post findPost = findPost(postIdx);
        verifyWriter(postIdx, memberIdx);
        findPost.setContent(patchDto.getContent());
        return postRepository.save(findPost);
    }

    public void deletePost(Long postIdx, Long memberIdx) {
        Post findPost = findPost(postIdx);
        verifyWriter(postIdx, memberIdx);
        postRepository.delete(findPost);
    }

    public Post verifyWriter(Long postIdx, Long memberIdx) {
        findPost(postIdx);
        Optional<Post> optional = postRepository.findPostByPostIdxAndMember_MemberIdx(postIdx, memberIdx);
        return optional.orElseThrow(() -> new CustomException(INVALID_AUTH_TOKEN));
    }




}
