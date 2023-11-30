package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${discord.webhookURL}")
    private String url;

    @Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Seoul")
    public void sendNotification() {

    }
}
