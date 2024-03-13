package com.example.foodthought.exception;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.entity.Status;
import com.example.foodthought.exception.customException.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            String errorCode = entry.getKey();
            String errorMessage = entry.getValue();
            log.error("url: {}, 메세지: {}, 에러코드: {}, \n StachTrace: {}",request.getRequestURI(),errorCode,errorMessage,ex.fillInStackTrace());
        }
        return ResponseEntity.badRequest().body(ResponseDto.fail(400, errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(IllegalArgumentException ex, HttpServletRequest request) {
        if (ex.getMessage().contains("No enum constant com.example.foodthought.entity.Status.")) {
            String customMessage = ("상태값은 " + Arrays.toString(Status.values()) + "만 입력가능 합니다.");
            log.error("url: {}, 메세지: {}",request.getRequestURI(),ex.getMessage());
            return ResponseEntity.badRequest().body(ResponseDto.fail(400, customMessage));
        }
        log.error("url: {}, 메세지: {} \n stacktrace: {}",request.getRequestURI(),ex.getMessage(), ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(ResponseDto.fail(400, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("url: {}, 메세지: {} \n stacktrace: {}",request.getRequestURI(),ex.getMessage(), ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(ResponseDto.fail(400, ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request) {
        log.error("url: {}, 메세지: {} \n stacktrace: {}",request.getRequestURI(),ex.getMessage(), ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(ResponseDto.fail(400, ex.getMessage()));
    }

//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity handUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request) {
//        log.error("url {}, message : {}",request.getRequestURI(),ex.getMessage());
//        return ResponseEntity.badRequest().body(ResponseDto.fail(400, ex.getMessage()));
//    }


    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity handleBookNotFoundException(BookNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(BookSearchNotFoundException.class)
    public ResponseEntity handleBookSearchNotFoundException(BookSearchNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}, keyword: {}",
                request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus(), ex.getTitle());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getTitle() + ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity handleBoardNotFoundException(BoardNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity handleCommentNotFoundException(CommentNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity handlePermissionDeniedException(PermissionDeniedException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(SelfFollowException.class)
    public ResponseEntity handleSelfFollowException(SelfFollowException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(CommentMismatchException.class)
    public ResponseEntity handleCommentReplyNotAllowedException(CommentMismatchException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity handleImageNotFoundException(ImageNotFoundException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }


    @ExceptionHandler(BoardReplyNotAllowedException.class)
    public ResponseEntity handBoardReplyNotAllowedException(BoardReplyNotAllowedException ex, HttpServletRequest request) {
        log.error("url {}, message : {}, statusCode: {}", request.getRequestURI(), ex.getErrorCode().getMessage(), ex.getErrorCode().getStatus());
        return ResponseEntity.badRequest().body(ResponseDto.fail(ex.getErrorCode().getStatus().value(), ex.getErrorCode().getMessage()));
    }
}