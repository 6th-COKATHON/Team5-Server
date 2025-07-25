package cotato.hack.domain.celebrity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.hack.domain.celebrity.entity.Celebrity;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {
}