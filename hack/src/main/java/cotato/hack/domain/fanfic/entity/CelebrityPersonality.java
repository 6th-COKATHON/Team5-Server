package cotato.hack.domain.fanfic.entity;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CelebrityPersonality {
    private String name;
    private String personality;
    private String speakingStyle;
    private String characteristics;
    private String appealPoint;
    
    public static CelebrityPersonality getPersonalityByName(String name) {
        switch (name.toLowerCase().replaceAll("\\s+", "")) {
            case "아이유":
                return new CelebrityPersonality(
                    "아이유",
                    "따뜻하고 친근한 성격으로 진정성 있는 모습을 보여주는 아티스트",
                    "부드럽고 따뜻한 말투, 상대방을 배려하는 언어 사용",
                    "진솔한 감정 표현, 소탈하면서도 깊이 있는 대화, 상대방을 편안하게 만드는 분위기",
                    "진심어린 관심과 배려, 자연스러운 친밀감"
                );
            case "박보검":
                return new CelebrityPersonality(
                    "박보검",
                    "신사적이고 예의바른 성격으로 상대방을 존중하는 마음가짐",
                    "정중하고 따뜻한 말투, 상대방을 배려하는 존댓말 사용",
                    "세심한 배려와 매너, 진지하면서도 따뜻한 대화, 상대방의 이야기를 잘 들어주는 모습",
                    "신사적인 매너와 진정성 있는 관심"
                );
            case "방탄소년단":
            case "bts":
                return new CelebrityPersonality(
                    "BTS",
                    "열정적이고 꿈을 향해 나아가는 긍정적인 에너지",
                    "친근하고 활기찬 말투, 동료애를 중시하는 따뜻한 언어",
                    "서로를 격려하고 응원하는 모습, 꿈과 목표에 대한 열정적인 대화",
                    "함께 성장하고자 하는 마음, 서로에 대한 깊은 신뢰"
                );
            case "송중기":
                return new CelebrityPersonality(
                    "송중기",
                    "밝고 유쾌한 성격으로 주변을 즐겁게 만드는 매력",
                    "장난스럽고 재미있는 말투, 상황에 맞는 유머 구사",
                    "유머감각이 뛰어나고 분위기를 밝게 만드는 능력, 상대방을 웃게 만드는 재치",
                    "밝은 에너지와 재치있는 대화"
                );
            case "수지":
                return new CelebrityPersonality(
                    "수지",
                    "우아하고 단아한 성격으로 세련된 매력을 지닌 아티스트",
                    "부드럽고 품격있는 말투, 절제된 아름다움이 묻어나는 언어",
                    "차분하고 우아한 분위기, 깊이 있는 대화를 나누는 지적인 모습",
                    "우아한 매너와 지적인 대화"
                );
            default:
                return new CelebrityPersonality(
                    name,
                    "매력적이고 친근한 성격으로 상대방을 편안하게 만드는 아티스트",
                    "자연스럽고 따뜻한 말투, 상대방을 배려하는 언어 사용",
                    "진정성 있는 대화와 상대방에 대한 관심, 편안한 분위기 조성",
                    "자연스러운 매력과 진심어린 관심"
                );
        }
    }
}