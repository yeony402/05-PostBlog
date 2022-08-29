package com.example.intermediate.domain;

import com.example.intermediate.controller.request.MemberRequestDto;
import com.example.intermediate.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberCreateTest {
    MemberService memberService;
    @Test
    @DisplayName("정상 케이스")
    void createMember(){
        //given
        Long id;
        String nickname = "test1234";
        String password = "test1234";
        String passwordConfirm = "test1234";

        MemberRequestDto memberRequestDto = new MemberRequestDto(nickname, password, passwordConfirm);

        //when
        Member member = new Member(memberRequestDto);

        //then
        assertEquals(nickname, member.getNickname());
        assertEquals(password, member.getPassword());
    }

}