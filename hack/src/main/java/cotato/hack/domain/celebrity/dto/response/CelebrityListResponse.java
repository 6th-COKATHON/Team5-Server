package cotato.hack.domain.celebrity.dto.response;

import cotato.hack.domain.celebrity.entity.Celebrity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CelebrityListResponse {
    private List<CelebrityInfo> celebrities;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    
    @Getter
    @Builder
    public static class CelebrityInfo {
        private Long id;
        private String name;
        private String url;
        
        public static CelebrityInfo from(Celebrity celebrity) {
            return CelebrityInfo.builder()
                    .id(celebrity.getId())
                    .name(celebrity.getName())
                    .url(celebrity.getImageUrl())
                    .build();
        }
    }
}