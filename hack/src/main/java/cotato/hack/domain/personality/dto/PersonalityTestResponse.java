package cotato.hack.domain.personality.dto;

import cotato.hack.domain.personality.enums.PersonalityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonalityTestResponse {
    private String type;
    private String name;
    private String content;

    public static PersonalityTestResponse from(PersonalityType type) {
        return new PersonalityTestResponse(
            type.name(),
            type.getName(),
            type.getContent()
        );
    }
}