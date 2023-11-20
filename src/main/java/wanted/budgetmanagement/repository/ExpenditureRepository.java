package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.util.List;
import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    Optional<List<Expenditure>> findAllByUserId(Integer id);

}
