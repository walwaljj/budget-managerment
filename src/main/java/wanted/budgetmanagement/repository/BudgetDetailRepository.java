package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.budget.entity.BudgetDetail;

public interface BudgetDetailRepository extends JpaRepository<BudgetDetail, Long> {
}
