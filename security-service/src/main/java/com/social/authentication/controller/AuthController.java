package com.social.authentication.controller;

import com.social.authentication.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Object> login(@Valid @RequestBody LoginDto loginDto) {
        final var token = authService.getAuthToken(loginDto);
        final var userDto = authService.getUserByEmail(loginDto.getEmail());
        final var data = Map.of("user", userDto, "access_token", token, "auth_type", "Bearer");
        return new ApiResponse<>(true, data, "User Token Generated Successfully!!!!");
    }
    @GetMapping("/validate/token")
    @ResponseStatus(HttpStatus.OK)
    public String validateToken(@RequestParam("access_token") String accessToken) {
        String userName = authService.validateToken(accessToken);
        if(userName.isBlank()){
            return "NOT-VALID";
        }else{
            return "VALID";
        }
    }



}