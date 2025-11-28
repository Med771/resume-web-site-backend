package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/main")
public class MainCnt {

    @GetMapping(path = "/status")
    public HttpStatus status() {
        return HttpStatus.OK;
    }

    @GetMapping(path = "/photo/{image_path}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("image_path") String image_path) {
        return ResponseEntity.ok().body(null);
    }
}
