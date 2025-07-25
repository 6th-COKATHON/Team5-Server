package cotato.hack.domain.personality.controller;

import cotato.hack.common.response.AppResponse;
import cotato.hack.domain.personality.dto.PersonalityTestRequest;
import cotato.hack.domain.personality.dto.PersonalityTestResponse;
import cotato.hack.domain.personality.service.PersonalityTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/personality")
@RequiredArgsConstructor
@Tag(name = "성향 테스트", description = "성향 테스트 관련 API")
public class PersonalityTestController {
    
    private final PersonalityTestService personalityTestService;
    
    @PostMapping("/test")
    @Operation(summary = "성향 테스트 결과 분석", description = "5개 질문의 답변을 받아 성향 타입을 분석합니다")
    public AppResponse<PersonalityTestResponse> analyzePersonality(
            @Valid @RequestBody PersonalityTestRequest request) {
        
        PersonalityTestResponse response = personalityTestService.analyzePersonality(request);
        return AppResponse.ok(response);
    }
}