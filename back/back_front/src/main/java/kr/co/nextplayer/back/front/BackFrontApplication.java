package kr.co.nextplayer.back.front;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication()
public class BackFrontApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(BackFrontApplication.class)
            .resourceLoader(new JarResourceLoader())
            .run(args);
    }
}
