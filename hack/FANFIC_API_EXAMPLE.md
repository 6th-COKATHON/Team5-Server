# 팬픽 생성 API 사용 예시

## API 엔드포인트
```
POST /api/fanfic/generate
```

## 요청 예시

### 1. 아이유 + A타입 (리더형) + 지민 주인공 팬픽
```json
{
    "personalityType": "A",
    "celebrityName": "아이유",
    "protagonistName": "지민",
    "genre": "로맨틱",
    "length": "보통"
}
```

### 2. 박보검 + B타입 (센스형) + 수현 주인공 팬픽
```json
{
    "personalityType": "B",
    "celebrityName": "박보검",
    "protagonistName": "수현",
    "genre": "로맨틱",
    "length": "긺"
}
```

### 3. BTS + C타입 (분위기메이커형) + 민수 주인공 팬픽
```json
{
    "personalityType": "C",
    "celebrityName": "방탄소년단",
    "protagonistName": "민수",
    "genre": "우정",
    "length": "짧음"
}
```

## 응답 예시
```json
{
    "status": "SUCCESS",
    "message": "요청이 성공적으로 처리되었습니다.",
    "data": {
        "chapters": [
            {
                "chapterNumber": 1,
                "content": "햇살은 노랗게 번지고, 강변을 따라 걷는 바람은 얇은 옷깃을 살짝 흔들었다. 그는 한 발 늦게 내 옆에 걷고 있었다. 일정도, 장소도, 준비한 커피까지 다. <em>그런 그의 모습이 어색하지 않았다. 오히려 자연스럽게 느껴졌다.</em>",
                "characterCount": 125
            },
            {
                "chapterNumber": 2,
                "content": "<span style=\"color: #FF69B4; font-weight: bold;\">\"지민씨, 혹시 시간 괜찮다면 저기 카페에서 잠깐 쉬어갈까요?\"</span> 그의 제안에 나는 고개를 끄덕였다. <strong>그 말이, 단순한 커피 한 잔을 받아들이는 게 아니라, 나라는 사람을 조금 받아들이겠다는 말처럼 들렸다.</strong>",
                "characterCount": 134
            },
            {
                "chapterNumber": 3,
                "content": "우리는 조용히 앉아, 아무 말 없이 노을을 봤다. <em>그리고 그 정적 속에서 나는 처음으로 느꼈다. 이런 고요함이 어색하지 않다는 걸. 오히려 편안하다는 걸.</em> <strong>그와 함께하는 이 시간이 특별하다는 걸.</strong>",
                "characterCount": 125
            },
            {
                "chapterNumber": 4,
                "content": "<span style=\"color: #FF69B4; font-weight: bold;\">\"지민씨, 오늘 정말 좋았어요.\"</span> 그의 진심 어린 말에 내 마음도 따뜻해졌다. <em>앞으로도 이런 시간들이 계속되길 바라며,</em> 우리는 서로를 바라보며 미소 지었다. <strong>새로운 시작을 알리는 그 미소와 함께.</strong>",
                "characterCount": 128
            }
        ],
        "fullContent": "## 1장\n햇살은 노랗게 번지고, 강변을 따라 걷는 바람은 얇은 옷깃을 살짝 흔들었다...\n\n## 2장\n\"지민씨, 혹시 시간 괜찮다면...\n\n## 3장\n지민은 조용히 앉아...\n\n## 4장\n\"지민씨, 오늘 정말 좋았어요.\"...",
        "celebrityName": "아이유",
        "protagonistName": "지민",
        "personalityType": "믿고 따를 수 있는 리더형",
        "genre": "로맨틱",
        "totalCharacterCount": 542
    }
}
```

## 지원하는 연예인 목록
- 아이유
- 박보검  
- 방탄소년단 (BTS)
- 송중기
- 수지
- 기타 (기본 성격으로 매핑됨)

## 성격 유형 (PersonalityType)
- A: 믿고 따를 수 있는 리더형
- B: 눈치 빠른 센스형
- C: 에너지 넘치는 분위기 메이커형
- D: 조용한 감성 충전형
- A_B: 계획형 리더 + 눈치 100단
- A_C: 주도형 + 매력 뿜뿜형
- A_D: 든든한 리더 + 따뜻한 감성형
- B_C: 센스 만렙 + 텐션 담당형
- B_D: 공감형 조율자 + 감성 챙김러
- C_D: 감성 뿜뿜 + 유쾌 내면형
- MIXED: 균형잡힌 올라운더형

## 팬픽 길이 옵션 (4장 구성, 각 장 300자 내외로 조정됨)
- "짧음": 각 장당 250-350자, 총 1000-1400자
- "보통": 각 장당 280-320자, 총 1120-1280자 (기본값)  
- "긺": 각 장당 300-400자, 총 1200-1600자

## 개인 맞춤형 팬픽 특징
- **주인공 이름**: 요청시 제공한 이름이 팬픽 전체에 자연스럽게 포함됨
- **개인화된 스토리**: 주인공의 성격 유형에 맞는 상황과 대화 구성
- **몰입감 증대**: 실제 이름 사용으로 더욱 생생한 팬픽 경험
- **HTML 마크다운 형식**: 시각적 효과가 포함된 풍부한 표현

## HTML 스타일링 요소
- **대화**: <span style="color: #FF69B4; font-weight: bold;">"핑크색 강조"</span>
- **감정 표현**: <em>기울임체</em>로 내면 묘사
- **클라이맥스**: <strong>굵은 글씨</strong>로 중요 순간 강조

## 응답 구조
- **chapters**: 각 장별 상세 정보 (장 번호, 내용, 글자 수)
- **fullContent**: 전체 팬픽 내용 (마크다운 형식)
- **protagonistName**: 주인공(사용자) 이름
- **totalCharacterCount**: 총 글자 수

## 4장 구성 스토리 흐름
- **1장**: 첫 만남 또는 일상적인 상황에서의 특별한 순간
- **2장**: 서로에 대한 관심이 생기고 대화가 깊어지는 과정  
- **3장**: 감정이 고조되고 서로의 마음을 확인하는 순간
- **4장**: 따뜻한 결말과 미래에 대한 희망적인 마무리