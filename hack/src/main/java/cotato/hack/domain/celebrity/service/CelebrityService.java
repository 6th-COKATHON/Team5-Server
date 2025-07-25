package cotato.hack.domain.celebrity.service;

import cotato.hack.domain.ai.service.ChatService;
import cotato.hack.domain.celebrity.dto.request.CelebrityMatchRequest;
import cotato.hack.domain.celebrity.dto.request.CelebritySaveRequest;
import cotato.hack.domain.celebrity.dto.response.CelebrityListResponse;
import cotato.hack.domain.celebrity.dto.response.CelebrityChemistryScore;
import cotato.hack.domain.celebrity.dto.response.CelebrityMatchResponse;
import cotato.hack.domain.celebrity.dto.response.CelebritySaveResponse;
import cotato.hack.domain.celebrity.entity.Celebrity;
import cotato.hack.domain.celebrity.entity.Gender;
import cotato.hack.domain.celebrity.repository.CelebrityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CelebrityService {
    
    private final CelebrityRepository celebrityRepository;
    private final ChatService chatService;

    // 연예인 저장
    @Transactional
    public CelebritySaveResponse saveCelebrity(CelebritySaveRequest request) {
        Celebrity celebrity = Celebrity.builder()
                .name(request.getName())
                .gender(request.getGender())
                .build();
        
        celebrity.updateCelebrityImageUrl(request.getUrl());
        Celebrity savedCelebrity = celebrityRepository.save(celebrity);
        
        return CelebritySaveResponse.from(savedCelebrity);
    }

    // 연예인 목록 조회
    public CelebrityListResponse getCelebrities(Gender gender, Pageable pageable) {
        Page<Celebrity> celebrityPage;
        
        if (gender != null) {
            celebrityPage = celebrityRepository.findByGender(gender, pageable);
        } else {
            celebrityPage = celebrityRepository.findAll(pageable);
        }
        
        List<CelebrityListResponse.CelebrityInfo> celebrities = celebrityPage.getContent()
                .stream()
                .map(CelebrityListResponse.CelebrityInfo::from)
                .toList();
        
        return CelebrityListResponse.builder()
                .celebrities(celebrities)
                .currentPage(celebrityPage.getNumber())
                .totalPages(celebrityPage.getTotalPages())
                .totalElements(celebrityPage.getTotalElements())
                .build();
    }

    // 연예인 궁합 분석 - 모든 연예인과의 케미점수 계산
    public CelebrityMatchResponse analyzeCelebrityMatch(CelebrityMatchRequest request) {
        List<Celebrity> selectedCelebrities = celebrityRepository.findAllById(request.getCelebrityIds());
        
        if (selectedCelebrities.isEmpty()) {
            throw new IllegalArgumentException("선택된 연예인이 존재하지 않습니다.");
        }
        
        // 모든 연예인과의 케미점수만 간단히 계산 (동점 방지)
        List<CelebrityChemistryScore> allScores = calculateAllChemistryScores(selectedCelebrities, request.getPersonalityType().getName());
        
        // 점수 순으로 정렬하여 가장 높은 점수의 연예인 찾기
        CelebrityChemistryScore bestMatch = allScores.get(0); // 이미 정렬되어 있음
        
        // 가장 잘 맞는 연예인에 대해서만 상세 분석 진행
        String detailedAnalysisPrompt = createDetailedAnalysisPrompt(bestMatch.getCelebrityName(), request.getPersonalityType().getName());
        String detailedAnalysisResult = chatService.getChatResponse(detailedAnalysisPrompt);
        
        String bestChemistryName = extractChemistryName(detailedAnalysisResult);
        String detailedAnalysis = extractAnalysis(detailedAnalysisResult);
        String advice = extractAdvice(detailedAnalysisResult);
        
        return CelebrityMatchResponse.builder()
            .allChemistryScores(allScores)
            .bestMatchCelebrityName(bestMatch.getCelebrityName())
            .bestChemistryName(bestChemistryName)
            .bestCompatibilityScore(bestMatch.getChemistryScore())
            .detailedAnalysis(detailedAnalysis)
            .advice(advice)
            .build();
    }

    // 모든 연예인과의 케미점수 계산 (동점 방지)
    private List<CelebrityChemistryScore> calculateAllChemistryScores(List<Celebrity> celebrities, String personalityType) {
        String allScoresPrompt = createAllScoresPrompt(celebrities, personalityType);
        String allScoresResult = chatService.getChatResponse(allScoresPrompt);
        
        List<CelebrityChemistryScore> scores = parseAllScores(allScoresResult, celebrities);
        
        // 점수 순으로 정렬 (높은 점수부터)
        scores.sort((a, b) -> Integer.compare(b.getChemistryScore(), a.getChemistryScore()));
        
        return scores;
    }
    
    // 모든 연예인의 점수를 한번에 계산하는 프롬프트 (동점 방지)
    private String createAllScoresPrompt(List<Celebrity> celebrities, String personalityType) {
        StringBuilder celebrityList = new StringBuilder();
        for (int i = 0; i < celebrities.size(); i++) {
            celebrityList.append(String.format("%d. %s\n", i + 1, celebrities.get(i).getName()));
        }
        
        return String.format("""
            사용자 성격: %s
            
            다음 연예인들과의 케미점수를 계산해주세요 (동점 없이):
            %s
            
            각 연예인마다 70-98점 사이의 서로 다른 점수를 매겨주세요.
            동점이 나오지 않도록 모든 점수는 다르게 해주세요.
            
            형식 (번호 순서대로):
            1: 점수
            2: 점수
            3: 점수
            ...
            
            예시:
            1: 95
            2: 88
            3: 82
            """, personalityType, celebrityList.toString());
    }
    
    // 모든 점수 파싱
    private List<CelebrityChemistryScore> parseAllScores(String result, List<Celebrity> celebrities) {
        List<CelebrityChemistryScore> scores = new ArrayList<>();
        String[] lines = result.split("\n");
        
        for (String line : lines) {
            if (line.matches("\\d+:\\s*\\d+")) {
                String[] parts = line.split(":");
                int index = Integer.parseInt(parts[0].trim()) - 1; // 1-based to 0-based
                int score = Integer.parseInt(parts[1].trim());
                
                if (index >= 0 && index < celebrities.size()) {
                    Celebrity celebrity = celebrities.get(index);
                    scores.add(new CelebrityChemistryScore(
                        celebrity.getId(),
                        celebrity.getName(),
                        score
                    ));
                }
            }
        }
        
        // 파싱 실패 시 기본 점수 부여 (동점 방지)
        if (scores.size() != celebrities.size()) {
            scores.clear();
            for (int i = 0; i < celebrities.size(); i++) {
                Celebrity celebrity = celebrities.get(i);
                // 90부터 시작해서 2씩 감소 (동점 방지)
                int score = 90 - (i * 2);
                scores.add(new CelebrityChemistryScore(
                    celebrity.getId(),
                    celebrity.getName(),
                    score
                ));
            }
        }
        
        return scores;
    }
    
    
    // 상세 분석을 위한 프롬프트
    private String createDetailedAnalysisPrompt(String celebrityName, String personalityType) {
        return String.format("""
            사용자 성격: %s
            연예인: %s
            
            위 조합의 연애 궁합을 상세히 분석하여 다음 형식으로 답변하세요:
            
            케미 분석: 3-4문장의 상세한 궁합 분석
            조언1: 첫 번째 관계 발전 조언(40자 이내)
            조언2: 두 번째 관계 발전 조언(40자 이내)
            조언3: 세 번째 관계 발전 조언(40자 이내)
            """, personalityType, celebrityName);
    }
    
    private String extractChemistryName(String text) {
        try {
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains("케미 타입:") || line.contains("우리 케미명:") || line.contains("케미명:")) {
                    int startIndex = line.indexOf("\"");
                    int endIndex = line.lastIndexOf("\"");
                    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                        return line.substring(startIndex + 1, endIndex);
                    }
                    // 따옴표가 없는 경우 콜론 뒤의 텍스트 추출
                    int colonIndex = line.indexOf(":");
                    if (colonIndex != -1) {
                        return line.substring(colonIndex + 1).trim().replaceAll("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            // 파싱 실패 시 기본값 반환
        }
        return "특별한 케미";
    }
    
    private int extractScore(String text) {
        try {
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains("케미 점수:") || line.contains("점수:")) {
                    String scoreStr = line.replaceAll("[^0-9]", "");
                    if (!scoreStr.isEmpty()) {
                        return Integer.parseInt(scoreStr.substring(0, Math.min(2, scoreStr.length())));
                    }
                }
            }
        } catch (Exception e) {
            // 파싱 실패 시 기본값 반환
        }
        return 85;
    }
    
    private String extractAnalysis(String text) {
        try {
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains("케미 분석:") || line.contains("분석:")) {
                    int colonIndex = line.indexOf(":");
                    if (colonIndex != -1) {
                        return line.substring(colonIndex + 1).trim();
                    }
                }
            }
        } catch (Exception e) {
            // 파싱 실패 시 기본값 반환
        }
        return "환상적인 케미를 자랑하는 조합입니다.";
    }
    
    private String extractAdvice(String text) {
        try {
            String[] lines = text.split("\n");
            StringBuilder advice = new StringBuilder();

            for (String line : lines) {
                if (line.contains("조언1:") || line.contains("조언2:") || line.contains("조언3:")) {
                    int colonIndex = line.indexOf(":");
                    if (colonIndex != -1) {
                        advice.append("✔️ ").append(line.substring(colonIndex + 1).trim()).append("\n");
                    }
                }
            }

            String result = advice.toString().trim();
            return result.isEmpty() ? "✔️ 서로의 차이점을 인정하고 존중해 주세요.\n✔️ 소통을 자주 나누며 감정을 공유해 보세요.\n✔️ 함께하는 시간을 소중히 여기세요." : result;
        } catch (Exception e) {
            return "✔️ 서로의 차이점을 인정하고 존중해 주세요.\n✔️ 소통을 자주 나누며 감정을 공유해 보세요.\n✔️ 함께하는 시간을 소중히 여기세요.";
        }
    }
}