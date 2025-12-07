package ru.ai.sin.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.config.FileConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileHelper {

    private final FileConfig fileConfig;

    public String saveFile(MultipartFile file, String fileName) {
        Path path = fileConfig.getFilePath();

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String finalFileName = fileName + extension;
        Path filePath = path.resolve(finalFileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return finalFileName;
        } catch (IOException e) {
            log.error("Failed to save file {} to path {}", finalFileName, filePath, e);
        }

        return null;
    }

    public byte[] getFileContent(String fileName) throws FileNotFoundException {
        Path filePath = fileConfig.getFilePath().resolve(fileName);

        if (!Files.exists(filePath)) {
            log.warn("File {} does not exist", fileName);
            throw new FileNotFoundException(fileName);
        }

        try (InputStream is = Files.newInputStream(filePath)) {
            return is.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to read file {}", fileName, e);
            throw new UncheckedIOException(e);
        }
    }


}
