package com.social.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import java.util.Map;

public record ApiError<T>(boolean success, @JsonProperty("error_code") HttpStatus errorCode, Map<String, T> data, String message) {
}
