package wanted.budgetmanagement.repository;

import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.time.LocalDate;
import java.util.List;

public interface ExpenditureRepositoryCustom {

    List<Expenditure> search(String category,
                             LocalDate date,
                             Integer min,
                             Integer max,
                             List<String> exceptCategory);
}
