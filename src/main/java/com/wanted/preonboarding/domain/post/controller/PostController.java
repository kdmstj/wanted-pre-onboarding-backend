package com.wanted.preonboarding.domain.post.controller;

import com.wanted.preonboarding.security.CustomUserDetails;
import com.wanted.preonboarding.security.service.MemberDetailsService;
import com.wanted.preonboarding.domain.member.service.MemberService;
import com.wanted.preonboarding.domain.post.dto.PostDto;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.mapper.PostMapper;
import com.wanted.preonboarding.domain.post.service.PostService;
import com.wanted.preonboarding.util.UriUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final PostMapper postMapper;
    private static final String DEFAULT_URI = "/posts";

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> postPost(@RequestBody PostDto.Post postDto, @AuthenticationPrincipal CustomUserDetails memberDetails){

        Post post = postMapper.postDtoToPost(postDto);
        post.setMember(memberService.findMember(memberDetails.getMemberIdx()));

        Post createdPost = postService.addPost(post);

        URI uri = UriUtil.createUri(DEFAULT_URI, createdPost.getPostIdx());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<?> getPostList(@RequestParam("pageNum") int pageNum){

        Page<Post> postList = postService.findPostList(pageNum);
        List<PostDto.Response> postDtoList = postMapper.postPageToResponseList(postList);
        return ResponseEntity.ok().body(postDtoList);
    }

    @GetMapping("/{post-idx}")
    public ResponseEntity<?> getPost(@PathVariable("post-idx") @Positive Long postIdx){

        Post findPost = postService.findPost(postIdx);
        PostDto.Response responseDto = postMapper.postToPostResponse(findPost);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/{post-idx}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> patchPost(@PathVariable("post-idx") @Positive Long postIdx, @RequestBody PostDto.Patch patchDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Post patchingPost = postMapper.patchDtoToPost(patchDto, postService.verifyWriter(postIdx, customUserDetails.getMemberIdx()));

        Post patchedPost = postService.patchPost(patchingPost);

        PostDto.Response responseDto = postMapper.postToPostResponse(patchedPost);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{post-idx}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePost(@PathVariable("post-idx") @Positive Long postIdx, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        postService.deletePost(postIdx, customUserDetails.getMemberIdx());

        return ResponseEntity.ok().build();
    }

}
