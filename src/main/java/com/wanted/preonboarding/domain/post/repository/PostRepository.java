package com.wanted.preonboarding.domain.post.repository;

import com.wanted.preonboarding.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByPostIdxAndMember_MemberIdx(Long postIdx, Long memberIdx);

}
