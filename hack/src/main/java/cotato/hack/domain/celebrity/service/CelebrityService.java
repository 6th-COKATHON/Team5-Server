package cotato.hack.domain.celebrity.service;

import cotato.hack.domain.ai.service.ChatService;
import cotato.hack.domain.celebrity.dto.request.CelebrityMatchRequest;
import cotato.hack.domain.celebrity.dto.request.CelebritySaveRequest;
import cotato.hack.domain.celebrity.dto.response.CelebrityListResponse;
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

import java.util.List;
import java.util.Random;

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

    // 연예인 궁합 분석
    public CelebrityMatchResponse analyzeCelebrityMatch(CelebrityMatchRequest request) {
        List<Celebrity> selectedCelebrities = celebrityRepository.findAllById(request.getCelebrityIds());
        
        if (selectedCelebrities.isEmpty()) {
            throw new IllegalArgumentException("선택된 연예인이 존재하지 않습니다.");
        }
        
        Celebrity bestMatch = selectedCelebrities.get(new Random().nextInt(selectedCelebrities.size()));
        
        String gptPrompt = createAnalysisPrompt(bestMatch.getName(), request.getPersonalityType().getName());
        String analysisResult = chatService.getChatResponse(gptPrompt);
        System.out.println(analysisResult);
        
        return parseAnalysisResult(analysisResult, bestMatch.getName());
    }

    // GPT 궁합 분석 프롬프트 생성
    private String createAnalysisPrompt(String celebrityName, String personalityType) {
        return String.format("""
            사용자 성격: %s
            연예인: %s
            
            위 조합의 연애 궁합을 분석하여 다음 형식으로 답변하세요:
            
            케미명: "창의적인 케미명"
            점수: 숫자만 (예: 92)
            분석: 2-3문장의 궁합 분석
            조언: 3개의 관계 발전 조언
            
            형식:
            우리 케미명: "케미명"
            케미 점수: 점수점
            케미 분석: 분석 내용
            조언1: 첫 번째 조언
            조언2: 두 번째 조언  
            조언3: 세 번째 조언
            """, personalityType, celebrityName);
    }
    
    private CelebrityMatchResponse parseAnalysisResult(String analysisResult, String celebrityName) {
        String chemistryName = extractChemistryName(analysisResult);
        int score = extractScore(analysisResult);
        String analysis = extractAnalysis(analysisResult);
        String advice = extractAdvice(analysisResult);

        return CelebrityMatchResponse.builder()
                .celebrityName(celebrityName)
                .chemistryName(chemistryName)
                .compatibilityScore(score)
                .analysis(analysis)
                .advice(advice)
                .build();
    }
    
    private String extractChemistryName(String text) {
        try {
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains("우리 케미명:") || line.contains("케미명:")) {
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