package com.vivek.social.authuser;

import com.vivek.social.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class AuthController {

    private final AuthService authService;

    private final ResourceLoader resourceLoader;

    @Value("${file.upload.location}")
    private String uploadLocation;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Object> login(@Valid @RequestBody LoginDto loginDto) {
        final var token = authService.getAuthToken(loginDto);
        final var userDto = authService.getUserByEmail(loginDto.getEmail());
        final var data = Map.of("user", userDto, "access_token", token, "auth_type", "Bearer");
        return new ApiResponse<>(true, data, "User Registered Successfully!!!!");
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JpaUserDto> register(
            @Valid @RequestPart("user") RegisterDto registerDto,
            @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {
        final var userDto = authService.saveUser(registerDto);
        saveFile(uploadLocation, image, registerDto.getEmail());
        return new ApiResponse<>(true, Map.of("user", userDto), "User Registered Successfully!!!!");
    }

    private void saveFile(String uploadDir, MultipartFile file, String fileName) throws IOException {
        Path path = getFolderPath(uploadDir);
        String imageName = getImageNameWithExtension(fileName, file);
        Files.copy(file.getInputStream(), path.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
    }

    private String getImageNameWithExtension(String newName, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex - 1);
        }
        return newName + extension;
    }

    private Path getFolderPath(String location) throws IOException {
        // Resolve the location
        File directory = resourceLoader.getResource("classpath:images/users").getFile();
        return Paths.get(directory.getAbsolutePath());
    }
}