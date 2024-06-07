package com.media.social.user.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class ImageService implements FileService {

    @Override
    public String save(String uploadDir, MultipartFile file, String fileName) throws IOException {
        Path path = getFolderPath(uploadDir);
        String imageName = getFilenameWithExtension(fileName, file);
        Files.copy(file.getInputStream(), path.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
        return imageName;
    }

    @Override
    public Path getFolderPath(String location)  {
        String currentDir = System.getProperty("user.dir");
        //creating dir if not exists
        File newDir = new File(currentDir, location);
        if (!newDir.exists()) {
            boolean isCreated = newDir.mkdir();
            if (isCreated) {
                log.info("Directory created: " + newDir.getAbsolutePath());
            } else {
                log.info("Failed to create directory: " + newDir.getAbsolutePath());

            }
        }

        return newDir.toPath();
    }

    @Override
    public byte[] getResource(Path path,String fileName) throws IOException {
        return Files.readAllBytes(path.resolve(fileName));
    }


    private String getFilenameWithExtension(String newName, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        assert originalFilename != null;
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return newName + extension;
    }

}
