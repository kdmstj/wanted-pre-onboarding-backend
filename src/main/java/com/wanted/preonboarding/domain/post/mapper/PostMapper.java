package com.wanted.preonboarding.domain.post.mapper;

import com.wanted.preonboarding.domain.post.dto.PostDto;
import com.wanted.preonboarding.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post postDtoToPost(PostDto.Post postDto);

    default Post patchDtoToPost(PostDto.Patch patchDto, Post post){
        if ( patchDto == null ) {
            return null;
        }

        post.setContent( patchDto.getContent() );

        return post;
    }

    PostDto.Response postToPostResponse(Post post);

    List<PostDto.Response> postPageToResponseList(Page<Post> volunteerList);
}
