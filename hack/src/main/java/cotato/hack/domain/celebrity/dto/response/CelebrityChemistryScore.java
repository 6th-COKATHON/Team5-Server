package cotato.hack.domain.celebrity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "연예인 케미점수 정보")
public class CelebrityChemistryScore {
    
    @Schema(description = "연예인 ID")
    private Long celebrityId;
    
    @Schema(description = "연예인 이름")
    private String celebrityName;
    
    @Schema(description = "케미점수", example = "92")
    private int chemistryScore;
    
    @Schema(description = "케미 타입명", example = "달콤한 허니케미")
    private String chemistryType;
}