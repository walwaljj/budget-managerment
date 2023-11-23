package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.budget.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
