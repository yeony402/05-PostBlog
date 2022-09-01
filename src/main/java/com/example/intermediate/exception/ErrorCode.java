package com.example.intermediate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 200 OK
    FAILURE_CONVERSION_FILE(HttpStatus.OK, "200_1", "파일 변환에 실패하였습니다!"),
    NOT_FOUND_POST(HttpStatus.OK, "200_2", "존재하지 않는 게시글입니다!"),

    // 400 Bad Request
    NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "400_1", "이미지 파일만 등록할 수 있습니다 고갱님^^");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

}
