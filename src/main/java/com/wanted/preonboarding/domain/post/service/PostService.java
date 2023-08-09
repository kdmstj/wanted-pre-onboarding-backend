package com.wanted.preonboarding.domain.post.service;

import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return optional.orElseThrow(() -> new RuntimeException("POST_NOT_FOUND"));
    }

    public Post patchPost(Post post) {
        return postRepository.save(post);
    }

    public Post verifyWriter(Long postIdx, Long memberIdx) {
        Optional<Post> optional = postRepository.findPostByPostIdxAndMember_MemberIdx(postIdx, memberIdx);
        return optional.orElseThrow(() -> new RuntimeException(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
    }

    public void deletePost(Long postIdx, Long memberIdx) {
        Post post = verifyWriter(postIdx, memberIdx);
        postRepository.delete(post);
    }
}
