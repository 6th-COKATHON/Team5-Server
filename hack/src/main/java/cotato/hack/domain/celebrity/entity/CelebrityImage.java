package cotato.hack.domain.celebrity.entity;

import cotato.hack.domain.image.domain.Image;
import cotato.hack.domain.image.domain.ImageExtension;
import cotato.hack.domain.image.domain.ImagePrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "celebrity_images")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CelebrityImage extends Image {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "celebrity_id", nullable = false, unique = true) // 유저별로 이미지는 하나만 존재
	private Long celebrityId;

	@Builder
	private CelebrityImage(
		Long celebrityId,
		String imagePath,
		String imageUrl,
		String fullImageName,
		String imageName,
		ImageExtension extension,
		ImagePrefix imagePurpose
	) {
		super(imagePath, imageUrl, fullImageName, imageName, extension, imagePurpose);
		this.celebrityId = celebrityId;
	}
}