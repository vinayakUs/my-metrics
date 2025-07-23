package org.example.accountservice.exceptions;

import jakarta.validation.ValidationException;
import org.example.accountservice.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class Advice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponseDto<String> handle(Exception e,WebRequest request) {
        return new ApiResponseDto<>(false, e.getMessage() , e.getClass().getName(),resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(ResourceAlreadyExist.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponseDto<String> handleResourceAlreadyExist(ResourceAlreadyExist e,WebRequest request) {

        return new ApiResponseDto<>(false, e.getMessage() , e.getClass().getName(),resolvePathFromWebRequest(request));
    }

    private String resolvePathFromWebRequest(WebRequest request) {
        try {
            return ((ServletWebRequest)request).getRequest().getRequestURI();
        }catch (Exception e) {
            return null;
        }
    }

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponseDto<String> handleResourceNotFound(ResourceNotFound e,WebRequest request) {

        return new ApiResponseDto<>(false, e.getMessage() , e.getClass().getName(),resolvePathFromWebRequest(request));


    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponseDto<Map<String,String>> handleValidationException(MethodArgumentNotValidException  e,WebRequest request) {
        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put("error", error.getDefaultMessage());  // fallback
            }        });
        return new ApiResponseDto<>(false, errors , e.getClass().getName(),resolvePathFromWebRequest(request));

    }




}
