package com.havefunwith;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ResController
 *
 * CloudController for testing EB deployment, Slack & Discord integration
 */
@RestController
public class CloudController {
    record Cloud(boolean inTheSky, String message) {}

    @GetMapping("/cloud")
    public Cloud getCloud() {
        return new Cloud(true, "App works!");
    }

    @GetMapping("/v2/cloud")
    public Cloud getCloudV2() {
        return new Cloud(true, "App works!");
    }
}