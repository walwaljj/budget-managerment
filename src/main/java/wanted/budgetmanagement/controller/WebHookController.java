package wanted.budgetmanagement.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wanted.budgetmanagement.service.WebHookService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@SecurityRequirement(name = "bearerAuth")
public class WebHookController {

    private final WebHookService webHookService;

    @PostMapping("/notification")
    public String postEvent(Authentication auth,
                            @RequestParam(value = "date", defaultValue = "today") String date) {

        if (date.equals("today")) {
            date = LocalDate.now().toString();
        }

        webHookService.sendNotification(auth.getName(), LocalDate.parse(date));
        return "이벤트 발생!";
    }
}
