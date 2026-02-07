package ru.ai.sin.logic.main;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.exception.models.NotFoundException;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final FileHelper fileHelper;

    @Override
    public byte[] getFileContent(String fileName) {
        try {
            return fileHelper.getFileContent(fileName);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("File not found");
        }
    }

    @Override
    public String getContentType(String imagePath) {
        if (imagePath.endsWith(".png")) {
            return "image/png";
        }
        if (imagePath.endsWith(".gif")) {
            return "image/gif";
        }
        if (imagePath.endsWith(".webp")) {
            return "image/webp";
        }

        return "image/jpeg";
    }
}
