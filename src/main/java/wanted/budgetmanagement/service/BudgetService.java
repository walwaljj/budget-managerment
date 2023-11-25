package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanted.budgetmanagement.domain.budget.dto.BudgetRequestDto;
import wanted.budgetmanagement.domain.budget.dto.BudgetResponseDto;
import wanted.budgetmanagement.domain.budget.entity.Budget;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.BudgetRepository;
import wanted.budgetmanagement.repository.UserRepository;

import java.time.Month;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    /**
     * 예산 설정하기
     */
    public BudgetResponseDto budgetSetting(String username ,BudgetRequestDto requestDto, Integer totalBudget){

        User user = findUserByUsername(username);

        Budget budget = Budget.builder()
                .userId(user.getId())
                .category(requestDto.getCategory())
                .budget(requestDto.getBudget())
                .month(requestDto.getMonth())
                .build();

        return BudgetResponseDto.toBudgetResponseDto(budgetRepository.save(budget));
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
     * @param user
     * @param month
     * @return
     */
    private Budget findBudgetByUserAndMonth(User user, Month month) {
        return budgetRepository.findByUserIdAndMonth(user.getId(), month).orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));
    }
}
