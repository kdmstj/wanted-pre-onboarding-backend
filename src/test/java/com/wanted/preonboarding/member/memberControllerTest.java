package com.wanted.preonboarding.member;

import com.google.gson.Gson;
import com.wanted.preonboarding.domain.member.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class memberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;


    @Test
    @DisplayName("정상 회원가입")
    void postMemberTest1() throws Exception{
        //given
        MemberDto.Post memberDto = MemberDto.Post.builder()
                .email("user1@gamil.com")
                .password("user1password")
                .build();

        String content = gson.toJson(memberDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/members")

                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/members/"))));
    }

    @Test
    @DisplayName("유효성 검사1 - 이메일 형식")
    void postMemberTest2() throws Exception{
        //given
        MemberDto.Post memberDto = MemberDto.Post.builder()
                .email("user1gmail.com")
                .password("password")
                .build();

        String content = gson.toJson(memberDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        //then
        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유효성 검사2 - 비밀번호 8자 미만")
    void postMemberTest3() throws Exception {
        //given
        MemberDto.Post memberDto = MemberDto.Post.builder()
                .email("user1@gamil.com")
                .password("pass")
                .build();

        String content = gson.toJson(memberDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        //then
        actions
                .andExpect(status().isBadRequest());
    }


}
