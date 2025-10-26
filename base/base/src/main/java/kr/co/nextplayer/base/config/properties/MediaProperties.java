package kr.co.nextplayer.base.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "nextplayer.media")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
public class MediaProperties {
    private final String fileServer;
    private final String storagePath;
    private final String ffmpegPath;
    private final String ffprobePath;
    private final Integer[] imageResizes;
    private final Integer[] videoResizes;
    private final String resourcePath;

}
