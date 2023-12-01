package wanted.budgetmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wanted.budgetmanagement.service.NotificationService;

@Configuration
public class WebhookConfig {

    @Bean
    public NotificationService notificationService(){
        return new NotificationService();
    }
}
