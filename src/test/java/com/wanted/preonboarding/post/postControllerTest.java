package com.wanted.preonboarding.post;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.wanted.preonboarding.auth.WithCustomUser;
import com.wanted.preonboarding.domain.member.dto.MemberDto;
import com.wanted.preonboarding.domain.member.entity.Member;
import com.wanted.preonboarding.domain.member.service.MemberService;
import com.wanted.preonboarding.domain.post.dto.PostDto;
import com.wanted.preonboarding.domain.post.entity.Post;
import com.wanted.preonboarding.domain.post.mapper.PostMapper;
import com.wanted.preonboarding.domain.post.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class postControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PostService postService;

    @MockBean
    private PostMapper postMapper;

    @Autowired
    private Gson gson;


    @BeforeAll
    static void beforeAll() {
        Member member = new Member(1L, "email", "passowrd");
    }



    @Test
    @WithCustomUser(email = "email", memberIdx = 1L)
    @DisplayName("정상 게시물 등록")
    void postMemberTest1() throws Exception{
        //given
        PostDto.Post postDto = new PostDto.Post("새로운 게시물 작성");
        given(postMapper.postDtoToPost(Mockito.any(PostDto.Post.class))).willReturn(new Post());
        Post mockResultPost = new Post();
        mockResultPost.setPostIdx(1L);

        given(postService.addPost(Mockito.any(Post.class))).willReturn(mockResultPost);

        String content = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/posts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
                actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/posts/"))));
    }

//    @Test
//    @DisplayName("게시물 조회")
//    void getPostListTest() throws Exception{
//
//
//        //given
//        Post post1 = new Post(1L, "게시물1", new Member());
//        Post post2 = new Post(2L, "게시물2", new Member());
//        Post post3 = new Post(3L, "게시물3", new Member());
//
//        Page<Post> pagePosts = new PageImpl<>(List.of(post1, post2, post3), PageRequest.of(0, 10, Sort.by("postIdx").descending()),2);
//
//        List<PostDto.Response> responses = List.of(new PostDto.Response(1L, "게시물1"),
//                new PostDto.Response(2L, "게시물2"),
//                new PostDto.Response(3L, "게시물3"));
//
//
//        // Stubbing by Mockito
//        given(postService.findPostList(Mockito.anyInt())).willReturn(pagePosts);
//        given(postMapper.postPageToResponseList(Mockito.any(Page.class))).willReturn(responses);
//
//        String page = "1";
//        String size = "10";
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("page", page);
//        queryParams.add("size", size);
//
//       ;
//
////        // when
////        ResultActions actions = mockMvc.perform(getRequestBuilder(uri, queryParams));
////
////        // then
////        MvcResult result = actions
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.data").isArray())
////                .andReturn();
////
////        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");
////        System.out.println(list);
////        assertThat(list.size(), is(2));
//    }




    @Test
    @DisplayName("특정 게시물 조회")
    void getPostTest() throws Exception{

    }

    @Test
    @DisplayName("게시물 수정")
    void patchPostTest() throws Exception{

    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePostTest() throws Exception{

    }
}
