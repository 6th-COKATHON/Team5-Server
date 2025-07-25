package cotato.hack.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GPTEmotionResponse {

	private int happiness;
	private int sadness;
	private int anger;
	private int fear;
	private int love;
	private int disgust;
	private int surprise;
	private int gratitude;
	private int regret;
	private int hope;
	private String devil_review;
	private String angel_review;
}
