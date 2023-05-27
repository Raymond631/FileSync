package com.example.filesync.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public CommonResponse exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return CommonResponse.error(e.getMessage());
    }
}
