package com.bqt.ecommerce.exceptions;

import com.bqt.ecommerce.payloads.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// it will interfere handle process of Controller
@RestControllerAdvice
public class CustomerExceptionHandler {


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handlerBadRequestException(BadRequestException ex, WebRequest webRequest) {
        ApiResponse response = new ApiResponse(false,ex.getMessage(),HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handlerNotFoundException(NotFoundException ex, WebRequest webRequest) {
        ApiResponse response = new ApiResponse(false,ex.getMessage(),HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> handlerUnauthorizedException(UnauthorizedException ex, WebRequest webRequest) {
        ApiResponse response = new ApiResponse(false,ex.getMessage(),HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
