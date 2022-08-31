package com.example.intermediate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import static com.example.intermediate.exception.ErrorCode.FAILURE_CONVERSION_FILE;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = { IllegalArgumentException.class })
//    protected ResponseEntity<ErrorResponse> handleDataException() {
//        log.error("handleDataException throw Exception : {}", FAILURE_CONVERSION_FILE);
//        return ErrorResponse.toResponseEntity(FAILURE_CONVERSION_FILE);
//    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
