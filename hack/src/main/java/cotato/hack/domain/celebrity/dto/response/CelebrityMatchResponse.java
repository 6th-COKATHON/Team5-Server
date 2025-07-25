package cotato.hack.domain.celebrity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@Schema(description = "연예인 궁합 분석 응답")
public class CelebrityMatchResponse {
    
    @Schema(description = "모든 연예인들의 케미점수")
    private List<CelebrityChemistryScore> allChemistryScores;
    
    @Schema(description = "가장 잘 맞는 연예인 이름", example = "아이유")
    private String bestMatchCelebrityName;
    
    @Schema(description = "최고 케미명", example = "달콤한 허니케미")
    private String bestChemistryName;
    
    @Schema(description = "최고 궁합 점수", example = "92")
    private int bestCompatibilityScore;
    
    @Schema(description = "상세 궁합 분석", example = "서로 다른 매력이 조화를 이루는 환상적인 조합입니다.")
    private String detailedAnalysis;
    
    @Schema(description = "관계 발전 조언", example = "✔️ 서로의 차이점을 인정하고 존중해 주세요.")
    private String advice;
}