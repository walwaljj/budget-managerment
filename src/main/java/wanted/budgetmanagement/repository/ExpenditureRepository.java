package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long>, ExpenditureRepositoryCustom {

    Optional<List<Expenditure>> findAllByUserId(Integer id);

    Optional<List<Expenditure>> findAllByUserIdAndDate(Integer id, LocalDate now);
}
