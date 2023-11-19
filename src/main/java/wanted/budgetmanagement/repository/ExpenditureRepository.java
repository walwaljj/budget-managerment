package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
}
