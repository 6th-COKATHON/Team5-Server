package cotato.hack.domain.celebrity.dto.response;

import cotato.hack.domain.celebrity.entity.Celebrity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CelebritySaveResponse {
    
    private Long id;
    private String name;
    private String url;
    
    public static CelebritySaveResponse from(Celebrity celebrity) {
        return new CelebritySaveResponse(
            celebrity.getId(),
            celebrity.getName(),
            celebrity.getImageUrl()
        );
    }
}