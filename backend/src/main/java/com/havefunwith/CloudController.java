package com.havefunwith;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ResController
 *
 * CloudController for testing Slack integration
 */
@RestController
public class CloudController {
    record Cloud(boolean inTheSky, String message) {}

//    Testing CI/CD with Slack Integration
    @GetMapping("/cloud")
    public Cloud getCloud() {
        return new Cloud(true, "App works!");
    }
}