package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureRequestDto;
import wanted.budgetmanagement.domain.expenditure.dto.ExpenditureResponseDto;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.repository.ExpenditureRepository;
import wanted.budgetmanagement.repository.UserRepository;
import wanted.budgetmanagement.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenditureService {
    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;

    /**
     * 지출 생성
     * @param expenditureRequestDto
     * @return ExpenditureResponseDto
     */
    @Transactional
    public ExpenditureResponseDto create(String username, ExpenditureRequestDto expenditureRequestDto){

        User user = findUserByUsername(username);

        Expenditure expenditure = ExpenditureRequestDto.fromRequestDto(user.getId(), expenditureRequestDto);

        return ExpenditureResponseDto.toResponseDto(expenditureRepository.save(expenditure));
    }

    /**
     * 지출 내역 삭제
     * @param username 로그인 유저 이름
     * @param expenditureId 지출 내역 Id
     */
    @Transactional
    public void delete(String username, Long expenditureId){

        User user = findUserByUsername(username);

        Expenditure expenditure = findExpenditureById(expenditureId);

        // 유저의 권한 확인
        checkPermissions(user, expenditure);

        expenditureRepository.delete(expenditure);
    }

    /**
     * 지출 내역 찾기
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
     * @param username
     * @return User
     */
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 로그인한 유저의 지출 내역에 대한 권한 확인
     */
    public boolean checkPermissions(User user, Expenditure expenditure){
        if(!user.getId().equals(expenditure.getUserId())){
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        return true;
    }


    /**
     * 지출 내역 수정
     * @param username
     * @param category
     * @param memo
     * @return
     */
    @Transactional
    public ExpenditureResponseDto update(String username, Long expenditureId, String category, String memo) {

        User user = findUserByUsername(username);

        Expenditure expenditure = findExpenditureById(expenditureId);

        checkPermissions(user, expenditure);

        // 지출 내역 수정
        expenditure.modify(category, memo);

        return ExpenditureResponseDto.toResponseDto(expenditureRepository.save(expenditure));
    }
}
