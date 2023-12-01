package wanted.budgetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.budgetmanagement.service.WebHookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class WebHookController {

    private final WebHookService webHookService;

    @PostMapping("/notification")
    public String postEvent() {

        webHookService.sendNotification();
        return "이벤트 발생!";
    }
}
