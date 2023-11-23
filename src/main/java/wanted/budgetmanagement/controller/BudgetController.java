package wanted.budgetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wanted.budgetmanagement.domain.budget.dto.BudgetRequestDto;
import wanted.budgetmanagement.response.CommonResponse;
import wanted.budgetmanagement.response.ResponseCode;
import wanted.budgetmanagement.service.BudgetService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> budgetSetting(Authentication auth, @RequestParam BudgetRequestDto budgetRequestDto){

        ResponseCode responseCode = ResponseCode.BUDGET_SETTING;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .data(budgetService.budgetSetting(auth.getName(), budgetRequestDto))
                .build());
    }

}
