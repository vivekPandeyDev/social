package com.social.user.authuser;

import com.social.user.operation.ImageService;
import com.social.user.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class AuthController {

    private final AuthService authService;

    private final ResourceLoader resourceLoader;

    private final ImageService imageService;

    @Value("${file.upload.location}")
    private String uploadLocation;

    @Value("${base.api.url}")
    private String baseUrl;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Object> login(@Valid @RequestBody LoginDto loginDto) {
        final var token = authService.getAuthToken(loginDto);
        final var userDto = authService.getUserByEmail(loginDto.getEmail());
        final var data = Map.of("user", userDto, "access_token", token, "auth_type", "Bearer");
        return new ApiResponse<>(true, data, "User Token Generated Successfully!!!!");
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JpaUserDto> register(
            @Valid @RequestPart("user") RegisterDto registerDto,
            @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {
        //saving file by user_name
        if(!Objects.isNull(image)) {
            String savedImageName = imageService.save(uploadLocation, image, registerDto.getUniqueName());
            registerDto.setProfileUrl(getProfileUrl(savedImageName));
        }
        final var userDto = authService.saveUser(registerDto);

        return new ApiResponse<>(true, Map.of("user", userDto), "User Registered Successfully!!!!");
    }

    private String getProfileUrl(String imageName){
        return String.format("%s/images/users/%s",baseUrl,imageName);
    }


}