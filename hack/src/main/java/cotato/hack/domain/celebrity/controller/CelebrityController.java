package cotato.hack.domain.celebrity.controller;

import cotato.hack.common.response.AppResponse;
import cotato.hack.domain.celebrity.dto.request.CelebrityMatchRequest;
import cotato.hack.domain.celebrity.dto.request.CelebritySaveRequest;
import cotato.hack.domain.celebrity.dto.response.CelebrityListResponse;
import cotato.hack.domain.celebrity.dto.response.CelebrityMatchResponse;
import cotato.hack.domain.celebrity.dto.response.CelebritySaveResponse;
import cotato.hack.domain.celebrity.entity.Gender;
import cotato.hack.domain.celebrity.service.CelebrityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping
    @Operation(summary = "연예인 목록 조회", description = "연예인 목록을 페이지네이션으로 조회합니다. 성별 필터링 가능")
    public AppResponse<CelebrityListResponse> getCelebrities(
            @Parameter(description = "성별 필터 (MAN, WOMAN)")
            @RequestParam(required = false) Gender gender,
            @Parameter(description = "페이지 정보 (page=0, size=10, sort=id)")
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        CelebrityListResponse response = celebrityService.getCelebrities(gender, pageable);
        return AppResponse.ok(response);
    }
    
    @PostMapping("/match")
    @Operation(summary = "연예인 궁합 분석", description = "선택한 연예인들과 성격 유형을 바탕으로 궁합을 분석합니다")
    public AppResponse<CelebrityMatchResponse> analyzeCelebrityMatch(
            @Valid @RequestBody CelebrityMatchRequest request
    ) {
        CelebrityMatchResponse response = celebrityService.analyzeCelebrityMatch(request);
        return AppResponse.ok(response);
    }
}