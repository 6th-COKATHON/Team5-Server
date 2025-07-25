package cotato.hack.domain.celebrity.service;

import cotato.hack.domain.celebrity.dto.request.CelebritySaveRequest;
import cotato.hack.domain.celebrity.dto.response.CelebritySaveResponse;
import cotato.hack.domain.celebrity.entity.Celebrity;
import cotato.hack.domain.celebrity.repository.CelebrityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CelebrityService {
    
    private final CelebrityRepository celebrityRepository;
    
    @Transactional
    public CelebritySaveResponse saveCelebrity(CelebritySaveRequest request) {
        Celebrity celebrity = Celebrity.builder()
                .name(request.getName())
                .build();
        
        celebrity.updateCelebrityImageUrl(request.getUrl());
        Celebrity savedCelebrity = celebrityRepository.save(celebrity);
        
        return CelebritySaveResponse.from(savedCelebrity);
    }
}