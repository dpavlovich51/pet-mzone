package com.mzone.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Sonia, I love you!";
    }

    @GetMapping("/hello-user")
    public String helloUser(Principal principal) {
        return "hello, " + principal.getName();
    }

}
