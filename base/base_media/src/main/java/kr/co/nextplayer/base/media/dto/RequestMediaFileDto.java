package kr.co.nextplayer.base.media.dto;

import lombok.*;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestMediaFileDto {
    
    private Flux<Part> files;
    private Boolean isResize;
    private String resizeType;
    private String memberCd;
    private String subPath;

    private String foreignId;
    private String foreignType;

}
