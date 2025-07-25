package cotato.hack.domain.personality.service;

import cotato.hack.common.error.ErrorCode;
import cotato.hack.common.error.exception.AppException;
import cotato.hack.domain.personality.dto.PersonalityTestRequest;
import cotato.hack.domain.personality.dto.PersonalityTestResponse;
import cotato.hack.domain.personality.enums.Answer;
import cotato.hack.domain.personality.enums.PersonalityType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonalityTestService {
    
    public PersonalityTestResponse analyzePersonality(PersonalityTestRequest request) {
        List<Answer> answers = request.getAnswers();
        
        if (answers.size() < 5) {
            throw new AppException(ErrorCode.NEED_MORE_ANSWERS_EXCEPTION);
        }
        
        Map<Answer, Integer> counts = new HashMap<>();
        counts.put(Answer.A, 0);
        counts.put(Answer.B, 0);
        counts.put(Answer.C, 0);
        counts.put(Answer.D, 0);
        
        for (Answer answer : answers) {
            counts.put(answer, counts.get(answer) + 1);
        }
        
        PersonalityType resultType = determineResultType(counts);
        return PersonalityTestResponse.from(resultType);
    }
    
    private PersonalityType determineResultType(Map<Answer, Integer> counts) {
        int maxCount = Collections.max(counts.values());
        
        if (maxCount == 1) {
            return PersonalityType.MIXED;
        }
        
        if (maxCount >= 3) {
            for (Map.Entry<Answer, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == maxCount) {
                    return PersonalityType.valueOf(entry.getKey().name());
                }
            }
        }
        
        if (counts.get(Answer.A) >= 2 && counts.get(Answer.B) >= 2) {
            return PersonalityType.A_B;
        } else if (counts.get(Answer.A) >= 2 && counts.get(Answer.C) >= 2) {
            return PersonalityType.A_C;
        } else if (counts.get(Answer.A) >= 2 && counts.get(Answer.D) >= 2) {
            return PersonalityType.A_D;
        } else if (counts.get(Answer.B) >= 2 && counts.get(Answer.C) >= 2) {
            return PersonalityType.B_C;
        } else if (counts.get(Answer.B) >= 2 && counts.get(Answer.D) >= 2) {
            return PersonalityType.B_D;
        } else if (counts.get(Answer.C) >= 2 && counts.get(Answer.D) >= 2) {
            return PersonalityType.C_D;
        }
        
        return PersonalityType.MIXED;
    }
}