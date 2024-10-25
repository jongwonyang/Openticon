package io.ssafy.openticon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController("member")
public class AuthController {
    @GetMapping("/auth/login")
    public String login() {
        System.out.println(LocalDateTime.now()+"/auth/login");
        return "Please login using OAuth2 providers.";
    }
}
