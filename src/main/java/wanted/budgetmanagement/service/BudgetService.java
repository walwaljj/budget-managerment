package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.budget.dto.BudgetRecommendationRequestDto;
import wanted.budgetmanagement.domain.budget.dto.BudgetRequestDto;
import wanted.budgetmanagement.domain.budget.dto.BudgetResponseDto;
import wanted.budgetmanagement.domain.budget.entity.Budget;
import wanted.budgetmanagement.domain.budget.entity.BudgetDetail;
import wanted.budgetmanagement.domain.expenditure.dto.TotalAmountResponseDto;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.BudgetDetailRepository;
import wanted.budgetmanagement.repository.BudgetRepository;
import wanted.budgetmanagement.repository.ExpenditureRepository;
import wanted.budgetmanagement.repository.UserRepository;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;
    private final BudgetDetailRepository budgetDetailRepository;

    /**
     * 예산 설정하기
     */
    public BudgetResponseDto budgetSetting(String username, BudgetRequestDto requestDto) {

        User user = findUserByUsername(username);

        Integer percentSum = 0;

        Integer amountSum = 0;

        for (BudgetDetail detail : requestDto.getBudgetDetail()) {
            percentSum += detail.getPercent();
            amountSum += detail.getAmount();
        }

        if (!percentSum.equals(100) || !amountSum.equals(requestDto.getBudget())) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT_OR_PERCENT);
        }

        List<BudgetDetail> budgetDetailList = budgetDetailRepository.saveAll(requestDto.getBudgetDetail());

        Budget budget = Budget.builder()
                .userId(user.getId())
                .budget(requestDto.getBudget())
                .month(requestDto.getMonth())
                .budgetDetails(budgetDetailList)
                .build();

        return BudgetResponseDto.toBudgetResponseDto(budgetRepository.save(budget));

    }

    /**
     * 예산 추천받기
     */
    public List<TotalAmountResponseDto> budgetRecommendation(String username, BudgetRecommendationRequestDto requestDto) {

        User user = findUserByUsername(username);

        // 설정한 예산이 있는지 확인, 있다면 BUDGET_EXISTS
        Optional<List<Budget>> optionalBudgetList = budgetRepository.findByUserIdAndMonth(user.getId(), requestDto.getMonth());

        if (!optionalBudgetList.get().isEmpty()) {
            throw new CustomException(ErrorCode.BUDGET_EXISTS);
        }

        // 총 예산
        Integer totalBudget = requestDto.getTotalBudget();

        //1. repo 에 저장된 금액을 모두 더하고
        List<Expenditure> allExpenditureList = expenditureRepository.findAll();

        int totalAmount = allExpenditureList.stream()
                .mapToInt(Expenditure::getAmount)
                .sum();

        //2. 총 금액에서 카테고리 별 비율을 나눠줌
        List<TotalAmountResponseDto> totalAmountResponseDtoList = expenditureRepository.totalAmountListByCategory(allExpenditureList);

        // 비율을 나눠 저장할 list
        List<TotalAmountResponseDto> recommendationAmountList = new ArrayList<>();

        List<BudgetDetail> budgetDetailList = new ArrayList<>();

        for (TotalAmountResponseDto totalAmountResponseDto : totalAmountResponseDtoList) {

            // 비율을 구함
            int rate = (int) ((double) totalAmountResponseDto.getAmount() / totalAmount * 100);

            //3. 사용자가 입력한 총 예산에서 비율 대로 금액을 카테고리 별로 나눠 주기
            int recommendationAmount = totalBudget * rate / 100;

            recommendationAmountList.add(TotalAmountResponseDto.builder()
                    .category(totalAmountResponseDto.getCategory())
                    .amount(recommendationAmount)
                    .build());

            BudgetDetail budgetDetail = BudgetDetail.builder()
                    .percent(rate)
                    .amount(recommendationAmount)
                    .category(totalAmountResponseDto.getCategory())
                    .month(requestDto.getMonth())
                    .build();

            budgetDetailList.add(budgetDetail);
        }

        budgetRepository.save(Budget.builder()
                .userId(user.getId())
                .budgetDetails(budgetDetailList)
                .month(requestDto.getMonth())
                .budget(requestDto.getTotalBudget())
                .build());

        return recommendationAmountList;
    }

    /**
     * 로그인한 유저 이름으로 으로 userEntity 찾기
     *
     * @param username
     * @return User
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 예산에 대한 권한 확인, 맞다면 Budget 반환
     */
    public Budget checkPermissions(String username, Long budgetId) {

        User user = findUserByUsername(username);

        Budget budget = findBudgetById(budgetId);

        if (!user.getId().equals(budget.getUserId())) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return budget;
    }

    /**
     * budgetId 로 예산 찾기
     *
     * @param budgetId
     * @return Expenditure
     */
    private Budget findBudgetById(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow(
                () -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));
        return budget;
    }


    /**
     * user 와 기간으로 budget 찾기
     *
     * @param user
     * @param month
     * @return
     */
    public List<Budget> findBudgetByUserAndMonth(User user, Month month) {
        return budgetRepository.findByUserIdAndMonth(user.getId(), month).orElseThrow(
                () -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));
    }
}
