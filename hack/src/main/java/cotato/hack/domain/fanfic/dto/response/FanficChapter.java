package cotato.hack.domain.fanfic.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "팬픽 장 정보")
public class FanficChapter {
    
    @Schema(description = "장 번호", example = "1")
    private int chapterNumber;
    
    @Schema(description = "장 내용", example = "햇살은 노랗게 번지고, 강변을 따라 걷는 바람은...")
    private String content;
    
    @Schema(description = "장 글자 수", example = "287")
    private int characterCount;
}