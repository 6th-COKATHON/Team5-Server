package cotato.hack.domain.personality.dto;

import cotato.hack.domain.personality.enums.Answer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PersonalityTestRequest {
    @NotNull
    private List<Answer> answers;
}