package com.media.social.user.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    String save(String uploadDir, MultipartFile file, String fileName) throws IOException;

    Path getFolderPath(String location);
    byte[] getResource(Path path,String fileName) throws IOException;
}
