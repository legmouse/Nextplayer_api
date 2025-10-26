package kr.co.nextplayer.back.login;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication()
public class BackLoginApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(BackLoginApplication.class)
            .resourceLoader(new JarResourceLoader())
            .run(args);
    }
}