package com.example.foodthought.exception;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.entity.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        if (ex.getMessage().contains("No enum constant com.example.foodthought.entity.Status.")) {
            String customMessage = ("상태값은 " + Arrays.toString(Status.values()) + "만 입력가능 합니다.");
            return ResponseEntity.badRequest().body(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(), customMessage));
        }
        return ResponseEntity.badRequest().body(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleIllegalArgumentExceptions(AuthenticationException ex) {
        return ResponseEntity.badRequest().body(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleIllegalArgumentExceptions(MissingServletRequestPartException ex) {
        return ResponseEntity.badRequest().body(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}