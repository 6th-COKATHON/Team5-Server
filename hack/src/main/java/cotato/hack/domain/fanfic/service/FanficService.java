package cotato.hack.domain.fanfic.service;

import cotato.hack.domain.ai.service.ChatService;
import cotato.hack.domain.fanfic.dto.request.FanficGenerateRequest;
import cotato.hack.domain.fanfic.dto.response.FanficChapter;
import cotato.hack.domain.fanfic.dto.response.FanficGenerateResponse;
import cotato.hack.domain.fanfic.entity.CelebrityPersonality;
import cotato.hack.domain.personality.enums.PersonalityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FanficService {
    
    private final ChatService chatService;
    
    public FanficGenerateResponse generateFanfic(FanficGenerateRequest request) {
        // 연예인 성격 정보 가져오기
        CelebrityPersonality celebrityPersonality = CelebrityPersonality.getPersonalityByName(request.getCelebrityName());
        
        // 사용자 성격 유형 정보 가져오기
        PersonalityType userPersonality = request.getPersonalityType();
        
        // GPT 프롬프트 생성
        String prompt = createFanficPrompt(celebrityPersonality, userPersonality, request.getProtagonistName(), request.getGenre(), request.getLength());
        
        // GPT를 통해 팬픽 생성
        String fanficContent = chatService.getChatResponse(prompt);
        
        // GPT 응답을 파싱하여 각 장으로 구분
        List<FanficChapter> chapters = parseFanficChapters(fanficContent);
        
        // 전체 내용 재구성
        String fullContent = chapters.stream()
            .map(chapter -> "## " + chapter.getChapterNumber() + "장\n" + chapter.getContent())
            .reduce((ch1, ch2) -> ch1 + "\n\n" + ch2)
            .orElse("");
        
        // 총 글자 수 계산
        int totalCharacterCount = chapters.stream()
            .mapToInt(FanficChapter::getCharacterCount)
            .sum();
        
        return new FanficGenerateResponse(
            chapters,
            fullContent,
            request.getCelebrityName(),
            request.getProtagonistName(),
            userPersonality.getName(),
            request.getGenre(),
            totalCharacterCount
        );
    }

    private String createFanficPrompt(CelebrityPersonality celebrity, PersonalityType userType, String protagonistName, String genre, String length) {
        String lengthInstruction = getLengthInstruction(length);

        return String.format("""
        당신은 팬픽 작가입니다. 다음 조건에 맞는 개인 맞춤형 팬픽을 한국어로 4장 구성으로 작성해주세요.

        **연예인 정보:**
        - 이름: %s
        - 성격: %s
        - 말투: %s
        - 특징: %s
        - 매력포인트: %s

        **주인공(독자) 정보:**
        - 이름: %s
        - 성격 유형: %s
        - 성격 설명: %s

        **팬픽 조건:**
        - 장르: %s
        - 길이: %s
        - 분위기: 장르에 어울리는 몰입감 있는 분위기
        - 상황: 장르에 어울리는 자유로운 설정 (일상/판타지/스릴러/로맨스 등 가능)
        - 구성: 4장으로 나누어 작성

        **4장 구성 가이드:**
        - 각 장은 장르에 따라 자유롭게 구성해주세요
        - 매번 다양한 전개와 자극적인 스토리도 가능하게 자유롭게 창작해주세요
        - 예측 불가능한 긴장감, 감정의 진폭, 로맨틱하거나 스릴 있는 연출을 포함해도 좋습니다

        **작성 가이드라인:**
        1. 각 장을 명확히 구분하여 "## 1장", "## 2장", "## 3장", "## 4장"으로 표시해주세요
        2. 연예인의 실제 성격과 말투를 최대한 반영해주세요
        3. 주인공(%s)의 성격 유형에 맞는 상황과 대화를 만들어주세요
        4. 주인공의 이름(%s)을 자연스럽게 대화와 내레이션에 포함시켜주세요
        5. 장르에 어울리는 자극적이고 흥미로운 상황을 구성해주세요
        6. 자연스러운 대화와 감정 흐름을 중시해주세요
        7. 주인공 관점에서 서술하되, 주인공 이름을 적절히 사용해주세요
        8. 각 장마다 스토리가 자연스럽게 이어지도록 구성해주세요

        **HTML 마크다운 형식 요구사항:**
        9. 마크다운과 HTML을 함께 사용하여 작성해주세요
        10. 모든 대화 부분(쌍따옴표 안의 내용)은 <span style="color: #FF69B4; font-weight: bold;">"대화내용"</span> 형식으로 핑크색 강조해주세요
        11. 중요한 감정 표현이나 내면 묘사는 <em>기울임체</em>로 표현해주세요
        12. 특별한 순간이나 클라이맥스는 <strong>굵은 글씨</strong>로 강조해주세요

        %s

        4장 구성의 개인 맞춤형 팬픽을 시작해주세요:
        """,
            celebrity.getName(),
            celebrity.getPersonality(),
            celebrity.getSpeakingStyle(),
            celebrity.getCharacteristics(),
            celebrity.getAppealPoint(),
            protagonistName,
            userType.getName(),
            userType.getContent(),
            genre,
            length,
            protagonistName,
            protagonistName,
            lengthInstruction
        );
    }
    
    private List<FanficChapter> parseFanficChapters(String fanficContent) {
        List<FanficChapter> chapters = new ArrayList<>();
        
        // ## 1장, ## 2장, ## 3장, ## 4장 패턴으로 분리
        Pattern chapterPattern = Pattern.compile("##\\s*(\\d+)장\\s*\\n([\\s\\S]*?)(?=##\\s*\\d+장|$)");
        Matcher matcher = chapterPattern.matcher(fanficContent);
        
        while (matcher.find()) {
            int chapterNumber = Integer.parseInt(matcher.group(1));
            String content = matcher.group(2).trim();
            
            // 각 장을 300자 정도로 조정
            String adjustedContent = adjustChapterLength(content, 300);
            
            // HTML 태그를 제외한 실제 텍스트 길이 계산
            int actualTextLength = calculateActualTextLength(adjustedContent);
            
            chapters.add(new FanficChapter(
                chapterNumber, 
                adjustedContent, 
                actualTextLength
            ));
        }
        
        // 만약 파싱에 실패했다면 전체 내용을 4등분
        if (chapters.isEmpty()) {
            log.warn("팬픽 장 구분 파싱 실패, 전체 내용을 4등분합니다.");
            chapters = divideFanficIntoChapters(fanficContent);
        }
        
        return chapters;
    }
    
    private String adjustChapterLength(String content, int targetLength) {
        // 공백과 줄바꿈 정리
        content = content.replaceAll("\\s+", " ").trim();
        
        // HTML 태그를 제외한 실제 텍스트 길이로 판단
        int actualLength = calculateActualTextLength(content);
        
        if (actualLength <= targetLength + 50) {
            // 목표 길이 근처면 그대로 반환
            return content;
        } else if (actualLength > targetLength + 50) {
            // 너무 길면 적절한 위치에서 자르기
            return truncateContentAtSentenceEnd(content, targetLength);
        }
        
        return content;
    }
    
    private int calculateActualTextLength(String content) {
        // HTML 태그를 제거하고 실제 텍스트 길이 계산
        return content.replaceAll("<[^>]*>", "").length();
    }
    
    private String truncateContentAtSentenceEnd(String content, int targetLength) {
        String plainText = content.replaceAll("<[^>]*>", "");
        
        if (plainText.length() <= targetLength) {
            return content;
        }
        
        // 목표 길이 근처에서 문장 끝 찾기
        String truncatedPlain = plainText.substring(0, Math.min(targetLength, plainText.length()));
        int lastPeriod = truncatedPlain.lastIndexOf('.');
        int lastExclamation = truncatedPlain.lastIndexOf('!');
        int lastQuestion = truncatedPlain.lastIndexOf('?');
        
        int cutPoint = Math.max(Math.max(lastPeriod, lastExclamation), lastQuestion);
        
        if (cutPoint > targetLength - 100 && cutPoint > 0) {
            // HTML 태그를 고려하여 원본 텍스트에서 해당 위치 찾기
            return findContentUpToPosition(content, cutPoint + 1);
        } else {
            // 적당한 문장 끝을 찾지 못한 경우
            return findContentUpToPosition(content, targetLength) + "...";
        }
    }
    
    private String findContentUpToPosition(String htmlContent, int plainTextPosition) {
        StringBuilder result = new StringBuilder();
        int plainTextCount = 0;
        boolean inTag = false;
        
        for (int i = 0; i < htmlContent.length() && plainTextCount < plainTextPosition; i++) {
            char c = htmlContent.charAt(i);
            
            if (c == '<') {
                inTag = true;
            }
            
            result.append(c);
            
            if (!inTag) {
                plainTextCount++;
            }
            
            if (c == '>') {
                inTag = false;
            }
        }
        
        return result.toString().trim();
    }
    
    private List<FanficChapter> divideFanficIntoChapters(String fanficContent) {
        List<FanficChapter> chapters = new ArrayList<>();
        String cleanContent = fanficContent.replaceAll("##\\s*\\d+장\\s*\\n?", "").trim();
        
        int totalLength = cleanContent.length();
        int chapterLength = totalLength / 4;
        
        for (int i = 0; i < 4; i++) {
            int start = i * chapterLength;
            int end = (i == 3) ? totalLength : (i + 1) * chapterLength;
            
            String chapterContent = cleanContent.substring(start, Math.min(end, totalLength));
            String adjustedContent = adjustChapterLength(chapterContent, 300);
            
            // HTML 태그를 제외한 실제 텍스트 길이 계산
            int actualTextLength = calculateActualTextLength(adjustedContent);
            
            chapters.add(new FanficChapter(
                i + 1,
                adjustedContent,
                actualTextLength
            ));
        }
        
        return chapters;
    }
    
    private String getLengthInstruction(String length) {
        switch (length.toLowerCase()) {
            case "짧음":
                return "- 분량: 각 장당 250-350자 정도로 총 1000-1400자의 4장 구성 팬픽으로 작성해주세요";
            case "보통":
                return "- 분량: 각 장당 280-320자 정도로 총 1120-1280자의 4장 구성 팬픽으로 작성해주세요";
            case "긺":
                return "- 분량: 각 장당 300-400자 정도로 총 1200-1600자의 4장 구성 팬픽으로 자세하게 작성해주세요";
            default:
                return "- 분량: 각 장당 280-320자 정도로 총 1120-1280자의 4장 구성 팬픽으로 작성해주세요";
        }
    }
}