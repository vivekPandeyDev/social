package com.social.user.exception;

import java.util.HashMap;
import java.util.Map;

import com.social.user.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("unused")
public class GlobalExceptionAdvice {

    private static final String MESSAGE = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiError<>(false, HttpStatus.BAD_REQUEST, errors, "Validation error, invalid field value passed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError<String> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        final var errors = new HashMap<String, String>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiError<>(false, HttpStatus.BAD_REQUEST, errors, "Validation error, invalid field value passed");
    }

    @ExceptionHandler(ServiceException.class)
    ProblemDetail handleMovieNotProblemDetail(ServiceException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setTitle(ex.getTitle());
        problemDetail.setType(
                ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).path("error").build().toUri());
        problemDetail.setProperties(Map.of(MESSAGE, ex.getMessage()));
        logMessage(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleResourceNotFound(NotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("CANNOT FIND RESOURCE");
        problemDetail.setType(
                ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).path("error").build().toUri());
        problemDetail.setProperties(Map.of(MESSAGE, ex.getMessage()));
        logMessage(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ApiError<String> badCredentialsException(BadCredentialsException ex) {
        return new ApiError<>(false, HttpStatus.UNAUTHORIZED, Map.of("error", ex.getMessage()),
                "Invalid credentials try with valid username or password");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail generalException(Exception ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Oops!!!! Something went wrong");
        problemDetail.setType(
                ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).path("error").build().toUri());
        problemDetail.setProperties(Map.of(MESSAGE, ex.getMessage()));
        logMessage(ex.getMessage());
        return problemDetail;
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
