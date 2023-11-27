package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.budget.entity.Budget;
import wanted.budgetmanagement.domain.expenditure.dto.*;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.BudgetRepository;
import wanted.budgetmanagement.repository.ExpenditureRepository;
import wanted.budgetmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenditureService {
    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;

    /**
     * 지출 생성
     *
     * @param expenditureRequestDto
     * @return ExpenditureResponseDto
     */
    @Transactional
    public ExpenditureResponseDto create(String username, ExpenditureRequestDto expenditureRequestDto) {

        User user = findUserByUsername(username);

        Expenditure expenditure = ExpenditureRequestDto.fromRequestDto(user.getId(), expenditureRequestDto);

        return ExpenditureResponseDto.toResponseDto(expenditureRepository.save(expenditure));
    }

    /**
     * 지출 내역 삭제
     *
     * @param username      로그인 유저 이름
     * @param expenditureId 지출 내역 Id
     */
    @Transactional
    public void delete(String username, Long expenditureId) {

        // 유저의 권한 확인
        Expenditure expenditure = checkPermissions(username, expenditureId);

        expenditureRepository.delete(expenditure);
    }

    /**
     * 지출 내역 찾기
     *
     * @param expenditureId
     * @return Expenditure
     */
    private Expenditure findExpenditureById(Long expenditureId) {
        Expenditure expenditure = expenditureRepository.findById(expenditureId).orElseThrow(
                () -> new CustomException(ErrorCode.EXPENDITURE_NOT_FOUND));
        return expenditure;
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
     * 로그인한 유저의 지출 내역에 대한 권한 확인, 맞다면 Expenditure 반환
     */
    public Expenditure checkPermissions(String username, Long expenditureId) {

        User user = findUserByUsername(username);

        Expenditure expenditure = findExpenditureById(expenditureId);

        if (!user.getId().equals(expenditure.getUserId())) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return expenditure;
    }


    /**
     * 지출 내역 수정
     *
     * @param username
     * @param category
     * @param memo
     * @return ExpenditureResponseDto
     */
    @Transactional
    public ExpenditureResponseDto update(String username, Long expenditureId, String category, String memo) {

        // 유저의 권한 확인
        Expenditure expenditure = checkPermissions(username, expenditureId);

        // 지출 내역 수정
        expenditure.modify(category, memo);

        return ExpenditureResponseDto.toResponseDto(expenditureRepository.save(expenditure));
    }

    /**
     * 지출 내역 상세 조회
     *
     * @param username
     * @param expenditureId
     * @return ExpenditureResponseDto
     */
    public ExpenditureResponseDto detail(String username, Long expenditureId) {

        // 유저의 권한 확인 및 지출 내역 반환
        Expenditure expenditure = checkPermissions(username, expenditureId);

        return ExpenditureResponseDto.toResponseDto(expenditure);
    }

    /**
     * 로그인 한 사용자의 지출 목록 조회
     *
     * @param username
     * @return List<ExpenditureResponseDto>
     */
    public List<ExpenditureResponseDto> expenditureList(String username) {

        User user = findUserByUsername(username);

        Optional<List<Expenditure>> expenditureList = expenditureRepository.findAllByUserId(user.getId());

        // 만약 userId 로 찾은 지출 목록의 결과가 비어있다면 notfound 반환
        if (expenditureList.isEmpty()) {
            throw new CustomException(ErrorCode.EXPENDITURE_NOT_FOUND);
        }

        return expenditureList.get().stream().map(ExpenditureResponseDto::toResponseDto).toList();
    }

    /**
     * 로그인 한 사용자의 금일 지출 목록 조회
     *
     * @param username
     * @return ExpenditureTotalAmountResponseDto
     */
    public ExpenditureTotalAmountResponseDto todayExpenditureList(String username, LocalDate date) {

        User user = findUserByUsername(username);

        List<Expenditure> todayExpenditureList = getExpenditureListByDate(user, date, date);

        Integer totalAmount = expenditureRepository.totalAmountByDate(date, date);

        ExpenditureTotalAmountResponseDto expenditureTotalAmountResponseDto = ExpenditureTotalAmountResponseDto.builder()
                .userId(user.getId())
                .expenditureList(todayExpenditureList)
                .totalAmountByDate(totalAmount)
                .build();

        return expenditureTotalAmountResponseDto;
    }

    /**
     * date 별 지출 조회
     *
     * @param user
     * @param start
     * @param end
     * @return List<Expenditure>
     */
    private List<Expenditure> getExpenditureListByDate(User user, LocalDate start, LocalDate end) {

        Optional<List<Expenditure>> todayExpenditureList = expenditureRepository.findAllByUserIdAndDateBetween(user.getId(), start, end);

        // 만약 userId 로 찾은 지출 목록의 결과가 비어있다면 notfound 반환
        if (todayExpenditureList.isEmpty()) {
            throw new CustomException(ErrorCode.EXPENDITURE_NOT_FOUND);
        }
        return todayExpenditureList.get();
    }

    /**
     * 조건 별 목록 조회
     *
     * @param category
     * @param date
     * @param min
     * @param max
     * @param exceptCategory
     * @return
     */
    public List<Expenditure> search(String category, LocalDate date, Integer min, Integer max, List<String> exceptCategory) {

        List<Expenditure> searchList = expenditureRepository.search(category, date, min, max, exceptCategory);

        // 만약 userId 로 찾은 지출 목록의 결과가 비어있다면 notfound 반환
        if (searchList.isEmpty()) {
            throw new CustomException(ErrorCode.EXPENDITURE_NOT_FOUND);
        }

        return searchList;
    }

    /**
     * 합계 조회
     *
     * @param username
     * @param category
     * @param date
     * @param min
     * @param max
     * @param exceptCategory
     * @return
     */
    public ExpenditureTotalAmountResponseDto totalAmount(String username, String category, LocalDate date, Integer min, Integer max, List<String> exceptCategory) {

        User user = findUserByUsername(username);

        // 조건 별 검색
        List<Expenditure> searchExpenditureList = search(category, date, min, max, exceptCategory);

        // 검색 결과를 이용해 카테고리별 금액 표출
        List<TotalAmountResponseDto> searchByCategoryAmountList = expenditureRepository.totalAmountListByCategory(searchExpenditureList);

        Integer totalAmount = expenditureRepository.totalAmountByDate(LocalDate.now(), LocalDate.now());

        ExpenditureTotalAmountResponseDto expenditureTotalAmountResponseDto = ExpenditureTotalAmountResponseDto.builder()
                .userId(user.getId())
                .expenditureList(searchExpenditureList)
                .totalAmountByCategory(searchByCategoryAmountList)
                .totalAmountByDate(totalAmount)
                .build();

        return expenditureTotalAmountResponseDto;
    }

    /**
     * 지출 통계 (지난 달 대비)
     *
     * @param username
     * @param date
     * @return
     */
    public ExpenditureStatisticsResponseDto statisticsByLastMonth(String username, LocalDate date) {

        ExpenditureStatisticsResponseDto expenditureStatisticsResponseDto = new ExpenditureStatisticsResponseDto();

        User user = findUserByUsername(username);

        // 지난달
        LocalDate lastMonth = date.minusMonths(1);

        // 유저의 지난달 소비 목록
        List<Expenditure> lastMonthExpenditureListByDate = getExpenditureListByDate(user, lastMonth.withDayOfMonth(1), lastMonth);

        // 지난 달 1일 ~ n 일 까지 소비 총액 == 100%
        Integer lastMonthTotalAmountByDate = expenditureRepository.totalAmountByDateAndUserId(lastMonthExpenditureListByDate, user.getId());

        // 지난 달 1일 ~ n 일 까지 카테고리 별 소비 총액
        List<TotalAmountResponseDto> lastMonthTotalAmountList = expenditureRepository.totalAmountListByCategory(lastMonthExpenditureListByDate);

        // 유저의 이번달 소비 목록
        List<Expenditure> thisMonthExpenditureListByDate = getExpenditureListByDate(user, date.withDayOfMonth(1), date);

        // 이번달 1일 ~ n 일 까지 소비 총액
        Integer totalAmountByDate = expenditureRepository.totalAmountByDateAndUserId(thisMonthExpenditureListByDate, user.getId());

        // 이번달 1일 ~ n 일 까지 카테고리 별 소비 총액
        List<TotalAmountResponseDto> totalAmountList = expenditureRepository.totalAmountListByCategory(thisMonthExpenditureListByDate);

        // 소비율 : 이번달 총액 ( 지난달 총액 / 100 )
        int rate = totalAmountByDate / (lastMonthTotalAmountByDate / 100);

        // 카테고리 별 소비율
        List<TotalAmountResponseDto> resultRateList = new ArrayList<>();

        for (Category category : Category.values()) {
            TotalAmountResponseDto lastMonthTotalAmountResponseDto = findTotalAmountDtoByCategory(lastMonthTotalAmountList, category);
            TotalAmountResponseDto totalAmountResponseDto = findTotalAmountDtoByCategory(totalAmountList, category);

            // 지난달 또는 이번달에 소비된 금액이 없다면 계산을 생략함.
            if (lastMonthTotalAmountResponseDto == null || totalAmountResponseDto == null) {
                continue;
            }

            TotalAmountResponseDto resultDto = new TotalAmountResponseDto();
            resultDto.setCategory(category);

            // 소비율 계산
            resultDto.setAmount((int) (double) totalAmountResponseDto.getAmount() / (lastMonthTotalAmountResponseDto.getAmount() / 100));

            resultRateList.add(resultDto);
        }

        return expenditureStatisticsResponseDto.builder()
                .today(date)
                .day(date.getDayOfWeek())
                .startDate(date.withDayOfMonth(1))
                .endDate(date)
                .thisMonthTotalAmount(totalAmountByDate)
                .searchedResultTotalAmount(lastMonthTotalAmountByDate)
                .rate(rate)
                .totalAmountByCategory(resultRateList)
                .build();
    }

    // 해당 카테고리에 대한 TotalAmountResponseDto 찾기
    private TotalAmountResponseDto findTotalAmountDtoByCategory(List<TotalAmountResponseDto> totalAmountList, Category category) {
        return totalAmountList.stream()
                .filter(dto -> dto.getCategory() == category)
                .findFirst()
                .orElse(null);
    }

    /**
     * 오늘 지출 추천받기
     *
     * @param username
     * @param date
     * @return
     */
    public ExpenditureRecommendationResponseDTO expenditureRecommendation(String username, LocalDate date) {

        User user = findUserByUsername(username);

        Optional<List<Budget>> optionalBudget = budgetRepository.findByUserIdAndMonth(user.getId(), date.getMonth());

        String message = "";

        // 만약 예산 설정이 되어있지 않다면 ? not found
        if (optionalBudget.isEmpty())
            throw new CustomException(ErrorCode.BUDGET_NOT_FOUND);

        List<Budget> budgetList = optionalBudget.get();

        Integer totalBudget = 0;

        for (Budget budget : budgetList) {
            totalBudget += budget.getBudget();
        }


        // 예산이 설정 되어 있다면
        // 입력받은 날짜 기준으로 1일~ 금일까지 지출 금액 합계 가져오기
        List<Expenditure> thisMonthExpenditureListByDate = getExpenditureListByDate(user, date.withDayOfMonth(1), date);
        Integer totalAmountByDate = expenditureRepository.totalAmountByDateAndUserId(thisMonthExpenditureListByDate, user.getId());

        // (한달 총 예산 - 지출 금액 합계) 구하기
        int remainingBudget = totalBudget - totalAmountByDate;

        // 마지막 일 까지 몇일 남았는지 계산 후 남은 예산에서 나누기.
        int remainingDays = date.lengthOfMonth() - date.getDayOfWeek().getValue();

        // ++ 예산이 너무 타이트하거나 예산을 초과했어도 0원 은 반환 금지 (유저가 설정한 예산 총 합계에서 해당 월의 일수를 나눠 반환)
        if (remainingBudget <= 0) {
            int amountPerDay = totalBudget / date.lengthOfMonth(); // 하루 사용 가능 금액
            remainingBudget = amountPerDay * remainingDays; // 새롭게 남은 예산을 세팅함.
            message = "잘 할 수 있어요! 조금 더 아껴 보아요";
        }

        int result = ((remainingBudget / remainingDays) / 100) * 100;

        if (!message.equals("")) message = "절약을 잘 실천하고 계세요! 오늘도 절약 도전!";

        return ExpenditureRecommendationResponseDTO.builder()
                .todayBudget(result)
                .date(date)
                .message(message)
                .build();
    }

}
