package com.social.authentication.util;

import com.social.authentication.response.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;

public class RequestUtil {

    public static <T> ParameterizedTypeReference<ApiResponse<T>> getResponseType(Class<T> clazz) {
        return new ParameterizedTypeReference<ApiResponse<T>>() {};
    }

    public static <T> ParameterizedTypeReference<ApiResponse<T>> getErrorType(Class<T> clazz) {
        return new ParameterizedTypeReference<ApiResponse<T>>() {};
    }

}
