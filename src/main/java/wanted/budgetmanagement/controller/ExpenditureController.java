package wanted.budgetmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureRequestDto;
import wanted.budgetmanagement.response.CommonResponse;
import wanted.budgetmanagement.response.ResponseCode;
import wanted.budgetmanagement.service.ExpenditureService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/expenditure")
@SecurityRequirement(name = "bearerAuth")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @PostMapping
    @Operation(summary = "지출 등록", description = "지출을 등록합니다.")
    public ResponseEntity<CommonResponse> expenditureCreate(
            @RequestBody ExpenditureRequestDto requestDto,
            Authentication auth
    ) {

        ResponseCode responseCode = ResponseCode.Expenditure_CREATE;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .data(expenditureService.create(auth.getName(), requestDto))
                .build());
    }

    @DeleteMapping("/id/{expenditureId}")
    @Operation(summary = "지출 내역 삭제", description = "지출을 삭제합니다.")
    public ResponseEntity<CommonResponse> expenditureDelete(
            @PathVariable Long expenditureId,
            Authentication auth
    ) {

        ResponseCode responseCode = ResponseCode.Expenditure_DELETE;

        expenditureService.delete(auth.getName(), expenditureId);

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build());
    }

    @PutMapping("/id/{expenditureId}")
    @Operation(summary = "지출 내역 수정", description = "지출을 수정합니다.")
    public ResponseEntity<CommonResponse> expenditureUpdate(
            @PathVariable Long expenditureId,
            String category, String memo,
            Authentication auth
    ) {

        ResponseCode responseCode = ResponseCode.Expenditure_UPDATE;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.update(auth.getName(), expenditureId, category, memo))
                .message(responseCode.getMessage())
                .build());
    }
}
