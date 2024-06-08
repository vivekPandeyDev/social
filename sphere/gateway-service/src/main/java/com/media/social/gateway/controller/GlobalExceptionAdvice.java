package com.media.social.gateway.controller;

import com.media.social.gateway.response.ApiError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("unused")
public class GlobalExceptionAdvice {

    private static final String MESSAGE = "message";

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError<String>> handleValidationExceptions(WebExchangeBindException ex) {
        final var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Mono.just(new ApiError<>(false, HttpStatus.BAD_REQUEST, errors, "Validation error, invalid field value passed"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError<String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        final var errors = new HashMap<String, String>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return Mono.just(new ApiError<>(false, HttpStatus.BAD_REQUEST, errors, "Validation error, invalid field value passed"));
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleServiceException(ServiceException ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setTitle(ex.getTitle());
        problemDetail.setType(
                UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).replacePath("/error").build().toUri());
        problemDetail.setProperties(Map.of(MESSAGE, ex.getMessage()));
        logMessage(ex.getMessage());
        return Mono.just(problemDetail);
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleServiceException(RestClientException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(ex.getMessage()));
    }

    private String extractPropertyName(PropertyReferenceException ex) {
        String message = ex.getMessage();
        int startIndex = message.indexOf("'") + 1;
        int endIndex = message.lastIndexOf("'");
        return message.substring(startIndex, endIndex);
    }

    private void logMessage(String message) {
        log.info("**************************** Exception Handled Start ****************************");
        log.error("Error message: {}", message);
        log.info("**************************** Exception Handled End ****************************");
    }
}
