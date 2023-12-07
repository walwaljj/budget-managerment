package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.user.entity.Alert;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Optional<Alert> findByUserId(Integer userId);
}
