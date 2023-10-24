package com.havefunwith;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudController {

    record Cloud(boolean inTheSky) {}
    
    @GetMapping("/cloud")
    public Cloud getCloud() {
        return new Cloud(true);
    }
}