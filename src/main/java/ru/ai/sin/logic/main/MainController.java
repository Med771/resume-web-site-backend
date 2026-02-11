package ru.ai.sin.logic.main;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/main")
public class MainController {

    private final MainService mainService;

    @GetMapping(path = "status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void status() {}

    @GetMapping(path = "photo/{image_path}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "image_path") String imagePath) {
        byte[] bytes = mainService.getFileContent(imagePath);
        String contentType = mainService.getContentType(imagePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(bytes);
    }
}
