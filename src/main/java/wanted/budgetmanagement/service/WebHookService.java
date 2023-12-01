package wanted.budgetmanagement.service;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WebHookService {

    @Value("${discord.webhookURL}")
    private String url;

//    @Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Seoul")
    public void sendNotification() {
        JSONObject data = new JSONObject();

        data.put("content", "[알림] 이벤트가 발생하였습니다");

        send(data);
    }

    private void send(JSONObject object){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);
        restTemplate.postForObject(url, entity, String.class);
    }
}
