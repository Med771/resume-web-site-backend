package ru.ai.sin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig {
    @Value("${app.file.path}")
    private String filePath;

    public Path getFilePath() {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);

                System.out.println("Директория создана: " + path.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при создании директории: " + e.getMessage());
            }
        }

        return path;
    }
}
