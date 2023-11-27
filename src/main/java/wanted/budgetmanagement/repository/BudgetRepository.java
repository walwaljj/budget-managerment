package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.budget.entity.Budget;

import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<List<Budget>> findByUserIdAndMonth(Integer userId, Month month);
}
