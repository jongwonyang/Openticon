package io.ssafy.openticon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    public String hello() {
        return "hello";
    }
}
