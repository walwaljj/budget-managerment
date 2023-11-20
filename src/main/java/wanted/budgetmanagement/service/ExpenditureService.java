package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureRequestDto;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureResponseDto;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureTotalAmountResponseDto;
import wanted.budgetmanagement.domain.expenditure.dto.TotalAmountResponseDto;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.ExpenditureRepository;
import wanted.budgetmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenditureService {
    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;

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

        List<Expenditure> todayExpenditureList = getTodayExpenditureList(user, date);

        Integer totalAmount = expenditureRepository.todayTotalAmount(date);

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
     * @param date
     * @return List<Expenditure>
     */
    private List<Expenditure> getTodayExpenditureList(User user, LocalDate date) {

        Optional<List<Expenditure>> todayExpenditureList = expenditureRepository.findAllByUserIdAndDate(user.getId(), date);

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
        List<TotalAmountResponseDto> searchByCategoryAmountList = expenditureRepository.totalAmount(searchExpenditureList);

        Integer totalAmount = expenditureRepository.todayTotalAmount(LocalDate.now());

        ExpenditureTotalAmountResponseDto expenditureTotalAmountResponseDto = ExpenditureTotalAmountResponseDto.builder()
                .userId(user.getId())
                .expenditureList(searchExpenditureList)
                .totalAmountByCategory(searchByCategoryAmountList)
                .totalAmountByDate(totalAmount)
                .build();

        return expenditureTotalAmountResponseDto;
    }
}
