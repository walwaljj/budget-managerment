package wanted.budgetmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wanted.budgetmanagement.service.WebHookService;

@Configuration
public class WebhookConfig {

    @Bean
    public WebHookService notificationService(){
        return new WebHookService();
    }
}
