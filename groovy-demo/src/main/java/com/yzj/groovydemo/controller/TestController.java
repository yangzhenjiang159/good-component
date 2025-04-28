package com.yzj.groovydemo.controller;

import com.yzj.groovydemo.service.GroovyExecService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private GroovyExecService groovyExecService;

    @GetMapping("/exec")
    public String sendByNumber(String msg) {
        groovyExecService.exec();
        return "ok";
    }
}