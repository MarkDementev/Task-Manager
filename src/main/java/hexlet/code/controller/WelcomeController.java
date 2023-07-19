package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import hexlet.code.config.rollbar.RollbarConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/welcome")
    public String welcomeUser() {
        return "Welcome to Spring";
    }
}
