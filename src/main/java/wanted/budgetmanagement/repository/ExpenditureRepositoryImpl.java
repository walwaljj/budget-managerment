package wanted.budgetmanagement.repository;

import wanted.budgetmanagement.domain.Expenditure;
import wanted.budgetmanagement.repository.support.Querydsl4RepositorySupport;

public class ExpenditureRepositoryImpl extends Querydsl4RepositorySupport implements ExpenditureRepositoryCustom{
    public ExpenditureRepositoryImpl() {
        super(Expenditure.class);
    }
}