package cotato.hack.domain.celebrity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.hack.domain.celebrity.entity.CelebrityImage;

public interface CelebrityImageRepository extends JpaRepository<CelebrityImage, Long> {

	Optional<CelebrityImage> findByCelebrityId(Long celebrityId);

	boolean existsByImagePath(String imagePath);
}
