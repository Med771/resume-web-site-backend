package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.service.impl.MainService;
import ru.ai.sin.exception.models.NotFoundException;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class MainServImpl implements MainService {

    private final FileHelper fileHelper;

    @Override
    public byte[] getFileContent(String fileName) {
        try {
            return fileHelper.getFileContent(fileName);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("File not found");
        }
    }
}
