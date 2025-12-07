package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.service.impl.MainService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/main")
public class MainCnt {

    private final MainService mainService;

    @GetMapping(path = "/status")
    public HttpStatus status() {
        return HttpStatus.OK;
    }

    @GetMapping(path = "/photo/{image_path}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("image_path") String image_path) {
        byte[] bytes = mainService.getFileContent(image_path);

        String contentType = "image/jpeg";

        if (image_path.endsWith(".png")) {
            contentType = "image/png";
        } else if (image_path.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (image_path.endsWith(".webp")) {
            contentType = "image/webp";
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(bytes);
    }
}
