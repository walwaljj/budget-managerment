package wanted.budgetmanagement.repository;

import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.repository.support.Querydsl4RepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class ExpenditureRepositoryImpl extends Querydsl4RepositorySupport implements ExpenditureRepositoryCustom{
    public ExpenditureRepositoryImpl() {
        super(Expenditure.class);
    }

    @Override
    public List<Expenditure> search(String category,
                                    LocalDate date,
                                    Integer min,
                                    Integer max,
                                    List<String> exceptCategory) {
        return null;
    }
}
