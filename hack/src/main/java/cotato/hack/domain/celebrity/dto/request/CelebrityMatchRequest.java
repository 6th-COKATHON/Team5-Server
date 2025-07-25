package cotato.hack.domain.celebrity.dto.request;

import cotato.hack.domain.personality.enums.PersonalityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CelebrityMatchRequest {
    
    @NotNull(message = "연예인 ID 목록은 필수입니다")
    @Size(min = 1, max = 3, message = "연예인은 최소 1명, 최대 3명까지 선택 가능합니다")
    private List<Long> celebrityIds;
    
    @NotNull(message = "성격 유형은 필수입니다")
    private PersonalityType personalityType;
}