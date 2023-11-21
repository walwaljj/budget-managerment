package wanted.budgetmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureRequestDto;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.response.CommonResponse;
import wanted.budgetmanagement.response.ResponseCode;
import wanted.budgetmanagement.service.ExpenditureService;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/id/{expenditureId}")
    @Operation(summary = "지출 상세 조회", description = "지출을 상세하게 조회합니다.")
    public ResponseEntity<CommonResponse> expenditureDetail(
            @PathVariable Long expenditureId,
            Authentication auth
    ) {

        ResponseCode responseCode = ResponseCode.Expenditure_READ;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.detail(auth.getName(), expenditureId))
                .message(responseCode.getMessage())
                .build());
    }

    @GetMapping("")
    @Operation(summary = "지출 전체 목록 조회", description = "유저의 지출 전체 목록을 조회합니다.")
    public ResponseEntity<CommonResponse> expenditureList(
            Authentication auth
    ) {

        ResponseCode responseCode = ResponseCode.Expenditure_READ;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.expenditureList(auth.getName()))
                .message(responseCode.getMessage())
                .build());
    }

    @GetMapping("/today")
    @Operation(summary = "금일 지출 목록 조회", description = "금일 지출 목록과 합계를 조회합니다.")
    public ResponseEntity<CommonResponse> todayExpenditureList(
            Authentication auth,
            @RequestParam(value = "date", defaultValue = "today") String date
    ) {

        if (date.equals("today")) {
            date = LocalDate.now().toString();
        }

        ResponseCode responseCode = ResponseCode.Expenditure_READ;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.todayExpenditureList(auth.getName(), LocalDate.parse(date)))
                .message(responseCode.getMessage())
                .build());
    }

    @GetMapping("/search")
    @Operation(summary = "지출 조건 조회", description = "지출을 조건에 따라 조회합니다. 지출 합계를 포함합니다.")
    public ResponseEntity<CommonResponse> expenditureSearch(
            Authentication auth,
            @RequestParam(value = "category", required = false, defaultValue = "") String category,
            @RequestParam(value = "date") LocalDate date,
            @RequestParam(value = "min", required = false) Integer min, // 최소 금액
            @RequestParam(value = "max", required = false) Integer max, // 최대 금액, 금액 설정 하지 않았을 때 DB서 가장 큰 금액이 자동 설정되어 모든 목록이 조회됨
            @RequestParam(value = "except", required = false) List<String> exceptCategory
    ) {

        // 범위 체크
        if (max != null && min != null && (min > max || min < 0 || max < 0)) {
            throw new CustomException(ErrorCode.INVALID_RANGE);
        }

        ResponseCode responseCode = ResponseCode.Expenditure_UPDATE;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.totalAmount(auth.getName(), category, date, min, max, exceptCategory))
                .message(responseCode.getMessage())
                .build());
    }

    @GetMapping("/statistics/month")
        // 오늘이 월요일이면 지난 주 월요일에 대한 소비율
        // 오늘 기준 다른 유저와의 소비율
    @Operation(summary = "지출 통계", description = "지난달 대비 지출 통계를 조회합니다. 예 ) 오늘이 10일 이라면 지난달 1일~ 10일 까지 ")
    public ResponseEntity<CommonResponse> expenditureStatisticsByLastMonth(
            Authentication auth,
            @RequestParam(value = "date", defaultValue = "today") String date
    ) {

        if (date.equals("today")) {
            date = LocalDate.now().toString();
        }
        ResponseCode responseCode = ResponseCode.Expenditure_UPDATE;

        return ResponseEntity.ok(CommonResponse.builder()
                .responseCode(responseCode)
                .code(responseCode.getCode())
                .data(expenditureService.statisticsByLastMonth(auth.getName(), LocalDate.parse(date)))
                .message(responseCode.getMessage())
                .build());
    }

}
