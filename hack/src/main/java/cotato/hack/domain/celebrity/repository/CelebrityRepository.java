package cotato.hack.domain.celebrity.repository;

import cotato.hack.domain.celebrity.entity.Celebrity;
import cotato.hack.domain.celebrity.entity.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {
    Page<Celebrity> findByGender(Gender gender, Pageable pageable);
}