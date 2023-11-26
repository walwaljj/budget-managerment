package wanted.budgetmanagement.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wanted.budgetmanagement.domain.budget.dto.BudgetRecommendationRequestDto;
import wanted.budgetmanagement.domain.budget.dto.BudgetRequestDto;
import wanted.budgetmanagement.response.CommonResponse;
import wanted.budgetmanagement.response.ResponseCode;
import wanted.budgetmanagement.service.BudgetService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/budget")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> budgetSetting(
            Authentication auth,
            @RequestBody BudgetRequestDto budgetRequestDto) {

        ResponseCode responseCode = ResponseCode.BUDGET_SETTING;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .data(budgetService.budgetSetting(auth.getName(), budgetRequestDto))
                .build());
    }
    @PostMapping("/recommend")
    public ResponseEntity<CommonResponse> budgetRecommendation(
            Authentication auth,
            @RequestBody BudgetRecommendationRequestDto budgetRequestDto) {

        ResponseCode responseCode = ResponseCode.BUDGET_RECOMMENDATION;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .data(budgetService.budgetRecommendation(auth.getName(), budgetRequestDto))
                .build());
    }
}
