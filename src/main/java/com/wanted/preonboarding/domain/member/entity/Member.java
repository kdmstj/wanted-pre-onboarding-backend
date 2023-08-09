package com.wanted.preonboarding.domain.member.entity;

import com.wanted.preonboarding.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity @Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberIdx;
    private String email;
    private String password;

//    @Builder.Default
//    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private final List<Post> postList = new ArrayList<>();
}
