package cotato.hack.domain.fanfic.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "팬픽 생성 응답")
public class FanficGenerateResponse {
    
    @Schema(description = "팬픽 장별 내용")
    private List<FanficChapter> chapters;
    
    @Schema(description = "생성된 팬픽 전체 내용")
    private String fullContent;
    
    @Schema(description = "연예인 이름")
    private String celebrityName;
    
    @Schema(description = "주인공(사용자) 이름")
    private String protagonistName;
    
    @Schema(description = "사용자 성격 유형")
    private String personalityType;
    
    @Schema(description = "장르")
    private String genre;
    
    @Schema(description = "총 글자 수")
    private int totalCharacterCount;
}