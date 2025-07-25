package cotato.hack.domain.fanfic.dto.request;

import cotato.hack.domain.personality.enums.PersonalityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "팬픽 생성 요청")
public class FanficGenerateRequest {
    
    @NotNull(message = "성격 유형은 필수입니다")
    @Schema(description = "사용자의 성격 유형", example = "A")
    private PersonalityType personalityType;
    
    @NotBlank(message = "연예인 이름은 필수입니다")
    @Schema(description = "팬픽을 생성할 연예인 이름", example = "아이유")
    private String celebrityName;
    
    @NotBlank(message = "주인공(사용자) 이름은 필수입니다")
    @Schema(description = "팬픽의 주인공(사용자) 이름", example = "지민")
    private String protagonistName;
    
    @Schema(description = "팬픽 장르/분위기", example = "로맨틱", defaultValue = "로맨틱")
    private String genre = "로맨틱";
    
    @Schema(description = "팬픽 길이", example = "짧음", defaultValue = "보통")
    private String length = "보통";
}