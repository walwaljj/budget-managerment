package wanted.budgetmanagement.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.expenditure.dto.TotalAmountResponseDto;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.repository.support.Querydsl4RepositorySupport;

import java.time.LocalDate;
import java.util.List;

import static wanted.budgetmanagement.domain.expenditure.entity.QExpenditure.expenditure;

public class ExpenditureRepositoryImpl extends Querydsl4RepositorySupport implements ExpenditureRepositoryCustom {
    public ExpenditureRepositoryImpl() {
        super(Expenditure.class);
    }

    /**
     * 조건별 지출 조회
     *
     * @param category       // 카테고리 별 조회
     * @param date           // 일자 별 조회 //TODO 최소 기간 ~ 최대 기간 조회 가능하도록 변경하면 좋을것 같음.
     * @param min            // 최소 금액
     * @param max            // 최대 금액
     * @param exceptCategory // 제외할 카테고리 리스트
     * @return
     */
    @Override
    public List<Expenditure> search(String category,
                                    LocalDate date,
                                    Integer min,
                                    Integer max,
                                    List<String> exceptCategory) {

        return selectFrom(expenditure)
                .where(
                        searchCategory(category),
                        searchDate(date, date),
                        searchAmountRange(min, max)
                )
                .fetch();
    }

    /**
     * 날짜별 지출 목록을 조회
     *
     * @param date
     * @return
     */
    @Override
    public List<Expenditure> searchByDate(LocalDate date) {
        return selectFrom(expenditure)
                .where(searchDate(date.withDayOfMonth(1), date))
                .fetch();
    }

    /**
     * 카테고리 별 지출 합계
     *
     * @return List<TotalAmountResponseDto> // 카테고리 별 지출 합계
     */
    @Override
    public List<TotalAmountResponseDto> totalAmountListByCategory(List<Expenditure> expenditureList) {

        return select(Projections.constructor(TotalAmountResponseDto.class, expenditure.category, expenditure.amount.sum()))
                .from(expenditure)
                .where(expenditure.in(expenditureList))
                .groupBy(expenditure.category)
                .fetch();
    }

    @Override
    public List<TotalAmountResponseDto> totalAmountListByCategory(LocalDate date) {

        List<Expenditure> expenditureList = searchByDate(date);

        return select(Projections.constructor(TotalAmountResponseDto.class, expenditure.category, expenditure.amount.sum()))
                .from(expenditure)
                .where(expenditure.in(expenditureList))
                .groupBy(expenditure.category)
                .fetch();
    }
    /**
     * 요일에 따른 지출 합계
     *
     * @param startDate
     * @param endDate
     * @return Integer // 지출 합계
     */
    @Override
    public Integer totalAmountByDate(LocalDate startDate, LocalDate endDate) {

        return select(expenditure.amount.sum())
                .from(expenditure)
                .where(searchDate(startDate, endDate))
                .fetchOne();
    }

    /**
     * 카테고리 조회
     *
     * @param category // 조회할 카테고리
     */
    private BooleanExpression searchCategory(String category) {
        if (category.equals(null) || category.isBlank())
            return null;  // 세부 검색 하지 않으면 필터링 하지 않음.
        return expenditure.category.eq(Category.valueOf(category));
    }

    /**
     * 기간으로 조회
     *
     * @param startDate // 시작 기간
     * @param endDate   // 마지막 기간
     */
    private BooleanExpression searchDate(LocalDate startDate, LocalDate endDate) {
        return expenditure.date.between(startDate, endDate);
    }

    /**
     * 조회 할 금액 범위
     *
     * @param min // 최소
     * @param max // 최대
     */
    private BooleanExpression searchAmountRange(Integer min, Integer max) {

        // min 값이 null 이고 max 만 정해진 경우.
        if (max != null && min == null) {
            min = 0;
            return expenditure.amount.between(min, max);
        }
        // max 값이 null 이고 min 값만 정해진 경우
        else if (max == null && min != null) {
            max = Integer.MAX_VALUE;
            return expenditure.amount.between(min, max);
        }
        // min 값과 max 값이 모두 정해진 경우
        else if (max != null && min != null) {
            return expenditure.amount.between(min, max);
        }

        return null;// min 과 max 값이 정해져 있지 않을 때 모든값을 반환함.
    }

}
