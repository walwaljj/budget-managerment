package wanted.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.budgetmanagement.domain.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
