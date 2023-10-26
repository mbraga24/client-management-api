package com.havefunwith;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudController {
    record Cloud(boolean inTheSky) {}
    // Test CI/CD
    @GetMapping("/cloud")
    public Cloud getCloud() {
        return new Cloud(true);
    }
}