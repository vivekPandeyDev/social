package com.social.user.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class UserController {

    @Value("${file.upload.location}")
    private String uploadLocation;

    private final FileService fileService;

    @GetMapping(value = "/images/users/{uniqueName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserImage(@PathVariable("uniqueName") String uniqueName) throws IOException {
        log.info("unique name: {}",uniqueName);
        return fileService.getResource(fileService.getFolderPath(uploadLocation),uniqueName);
    }

}
