package cotato.hack.domain.celebrity.entity;

import static jakarta.persistence.EnumType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "celebrities")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Celebrity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(STRING)
	Gender gender;

	@Column(name = "image_url")
	private String imageUrl;

	@Builder
	private Celebrity(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}

	public void updateCelebrityImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
