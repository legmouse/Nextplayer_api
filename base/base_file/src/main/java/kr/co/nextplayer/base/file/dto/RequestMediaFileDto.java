package kr.co.nextplayer.base.file.dto;

import lombok.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestMediaFileDto {
    private Flux<FilePart> files;
    private String memberCd;
    private String subPath;
}
