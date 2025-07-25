package cotato.hack.domain.fanfic.controller;

import cotato.hack.common.response.AppResponse;
import cotato.hack.domain.fanfic.dto.request.FanficGenerateRequest;
import cotato.hack.domain.fanfic.dto.response.FanficGenerateResponse;
import cotato.hack.domain.fanfic.service.FanficService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fanfic")
@RequiredArgsConstructor
@Tag(name = "팬픽", description = "팬픽 생성 관련 API")
public class FanficController {
    
    private final FanficService fanficService;
    
    @PostMapping("/generate")
    @Operation(summary = "팬픽 생성", description = "사용자의 성격 유형과 연예인 정보를 바탕으로 맞춤형 팬픽을 생성합니다")
    public AppResponse<FanficGenerateResponse> generateFanfic(
            @Valid @RequestBody FanficGenerateRequest request
    ) {
        FanficGenerateResponse response = fanficService.generateFanfic(request);
        return AppResponse.ok(response);
    }
}