package com.example.intermediate.domain;

import com.example.intermediate.controller.request.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostUpdateTest {
    @Test
    @DisplayName("정상케이스")

    void updatepost(){
        String title = "테스트";
        String content = "안녕하세요";

        PostRequestDto requestDto = new PostRequestDto(title, content);
        Post post = new Post();
        post.update(requestDto);

        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }
}
