package kr.co.nextplayer.base.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

/**
 * Base Profiles
 */
@Getter
@Slf4j
@RequiredArgsConstructor
public class BaseProfiles {
    private final boolean prod;
    private final boolean local;
    private final boolean dev;
    private final boolean apiProd = false;
    private final String baseDomain = "mobile.nextplayer.co.kr";

    public BaseProfiles(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("activeProfiles : {}", activeProfiles);
        this.local = environment.acceptsProfiles(Profiles.of("local"));
        this.dev = environment.acceptsProfiles(Profiles.of("dev"));
        this.prod = environment.acceptsProfiles(Profiles.of("prod"));
        // 운영환경여부 set
        log.info("prod : {}", prod);
        log.info("local : {}", local);
        log.info("dev : {}", dev);
    }


}
