package com.media.social.user.app;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.dto.UserDto;
import com.media.social.user.file.FileService;
import com.media.social.user.file.ImageService;
import com.media.social.user.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;

    private final ResourceLoader resourceLoader;

    private final ImageService imageService;

    private final FileService fileService;

    @Value("${file.upload.location}")
    private String uploadLocation;

    @Value("${base.api.url}")
    private String baseUrl;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDto> register(
            @Valid @RequestPart("user") RegisterDto registerDto,
            @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {
        //saving file by user_name
        if(!Objects.isNull(image)) {
            String savedImageName = imageService.save(uploadLocation, image, registerDto.getUniqueName());
            registerDto.setProfileUrl(getProfileUrl(savedImageName));
        }
        final var userDto = userService.saveUser(registerDto);

        return new ApiResponse<>(true, Map.of("user", userDto), "User Registered Successfully!!!!");
    }

    @GetMapping("/emails/{email}")
    public ApiResponse<UserDto> getUserDetailByEmail(@PathVariable("email") String email){
        var savedUserDto = userService.getUserByEmail(email);
        return new ApiResponse<>(true, Map.of("user", savedUserDto), "User Detail Fetched Successfully!!!!");
    }

    @GetMapping("/{uniqueName}")
    public ApiResponse<UserDto> getUserDetailByUniqueName(@PathVariable("uniqueName") String uniqueName){
        var savedUserDto = userService.getUserByUniqueName(uniqueName);
        return new ApiResponse<>(true, Map.of("user", savedUserDto), "User Detail Fetched Successfully!!!!");
    }

    @GetMapping(value = "/images/{uniqueName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserImage(@PathVariable("uniqueName") String uniqueName) throws IOException {
        log.info("unique name: {}",uniqueName);
        return fileService.getResource(fileService.getFolderPath(uploadLocation),uniqueName);
    }

    private String getProfileUrl(String imageName){
        return String.format("%s/users/images/%s",baseUrl,imageName);
    }


}