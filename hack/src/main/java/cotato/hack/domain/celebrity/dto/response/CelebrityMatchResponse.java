package cotato.hack.domain.celebrity.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CelebrityMatchResponse {
    private String celebrityName;
    private String chemistryName;
    private int compatibilityScore;
    private String analysis;
    private String advice;
}