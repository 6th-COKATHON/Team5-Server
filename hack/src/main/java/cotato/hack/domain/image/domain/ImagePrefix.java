package cotato.hack.domain.image.domain;

import lombok.Getter;

@Getter
public enum ImagePrefix {
	PROFILE("profile"),
	;

	private final String prefix;

	ImagePrefix(String prefix) {
		this.prefix = prefix;
	}
}
