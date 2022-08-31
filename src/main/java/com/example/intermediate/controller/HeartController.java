package com.example.intermediate.controller;


import com.example.intermediate.controller.request.HeartCommentDto;
import com.example.intermediate.controller.request.HeartDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성
@RestController
public class HeartController {
    private final HeartService heartService;

    // 게시글 좋아요 0일때 (안 눌렀을 때) 실행
    @RequestMapping(value = "/api/heart", method = RequestMethod.POST)
    public ResponseDto<?> heart(@RequestBody @Valid HeartDto heartDto, HttpServletRequest request) {
        return heartService.postheart(heartDto, request);
    }

    // 댓글 좋아요 0일때 (안 눌렀을 때) 실행
    @RequestMapping(value = "/api/heart/comment", method = RequestMethod.POST)
    public ResponseDto<?> heart(@RequestBody @Valid HeartCommentDto heartDto, HttpServletRequest request) {
        return heartService.commentheart(heartDto, request);
    }

    // 게시글 좋아요가 1일때 (이미 눌렀을 때) 실행
    @RequestMapping(value = "/api/heart", method = RequestMethod.DELETE)
    public ResponseDto<?> unHeart(@RequestBody @Valid HeartDto heartDto, HttpServletRequest request) {
        return heartService.postunHeart(heartDto, request);
    }

    // 댓글 좋아요가 1일때 (이미 눌렀을 때) 실행
    @RequestMapping(value = "/api/heart/comment", method = RequestMethod.DELETE)
    public ResponseDto<?> unheart(@RequestBody @Valid HeartCommentDto heartDto, HttpServletRequest request) {
        return heartService.commentunHeart(heartDto, request);
    }
}
