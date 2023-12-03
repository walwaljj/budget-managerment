package wanted.budgetmanagement.service;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureRecommendationResponseDTO;

import java.time.LocalDate;

@Service
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class WebHookService {

    private final ExpenditureService expenditureService;

    @Value("${discord.webhookURL}")
    private String url;

//    @Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Seoul")
    public void sendNotification(String username, LocalDate date) {
        JSONObject data = new JSONObject();

        String content = getContent(username, date);

        data.put("content", content);

        send(data);
    }

    private void send(JSONObject object){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);
        restTemplate.postForObject(url, entity, String.class);
    }

    /**
     * 사용자에게 알릴 내용을 생성함.
     * @return 추천 지출
     */
    private String getContent(String username, LocalDate date){

        StringBuilder sb = new StringBuilder();
        sb.append(username + "님! 목표를 위해 "+ date + "( "+ date.getDayOfWeek() +" ) 사용 가능한 적절한 지출을 알려드릴게요!\n");

        ExpenditureRecommendationResponseDTO expenditureRecommendation = expenditureService.expenditureRecommendation(username, date);

        Integer todayBudget = expenditureRecommendation.getTodayBudget();

        sb.append("금일 총 사용 추천 금액 : " + todayBudget.toString()+"\n");

        sb.append(expenditureRecommendation.getMessage());

        sb.append("\n목표를 위해 화이팅~! ദി ᷇ᵕ ᷆ )♡ ");
        return sb.toString();
    }
}
