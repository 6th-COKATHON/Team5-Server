package cotato.hack.domain.celebrity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CelebritySaveRequest {
    
    @NotBlank(message = "연예인 이름은 필수입니다.")
    private String name;
    
    @NotBlank(message = "이미지 URL은 필수입니다.")
    private String url;
}