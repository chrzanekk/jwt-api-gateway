package org.konradchrzanowski.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/auth-fallback")
    Flux<Void> getAuthFallback() {
        log.info("Fallback from authentication service");
        return Flux.empty();
    }

    @GetMapping("/user-fallback")
    Flux<Void> getUserFallback() {
        log.info("Fallback from user service");
        return Flux.empty();
    }
}
