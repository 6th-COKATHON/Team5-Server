package cotato.hack.domain.celebrity.controller;

import cotato.hack.common.response.AppResponse;
import cotato.hack.domain.celebrity.dto.request.CelebritySaveRequest;
import cotato.hack.domain.celebrity.dto.response.CelebritySaveResponse;
import cotato.hack.domain.celebrity.service.CelebrityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/celebrity")
@RequiredArgsConstructor
@Tag(name = "연예인", description = "연예인 관련 API")
public class CelebrityController {
    
    private final CelebrityService celebrityService;
    
    @PostMapping
    @Operation(summary = "연예인 정보 저장", description = "연예인 이름과 이미지 URL을 저장합니다")
    public AppResponse<CelebritySaveResponse> saveCelebrity(
            @Valid @RequestBody CelebritySaveRequest request
    ) {
        CelebritySaveResponse response = celebrityService.saveCelebrity(request);
        return AppResponse.ok(response);
    }
}