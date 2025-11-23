package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin/main")
public class MainCnt {

    @GetMapping(path = "status")
    public HttpStatus status() {
        return HttpStatus.OK;
    }
}
