package kr.co.nextplayer.back.media;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;

@SpringBootApplication()
public class BackMediaApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(BackMediaApplication.class)
            .resourceLoader(new JarResourceLoader())
            .run(args);
    }
}
