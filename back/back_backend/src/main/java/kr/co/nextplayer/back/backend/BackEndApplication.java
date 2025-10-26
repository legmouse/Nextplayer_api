package kr.co.nextplayer.back.backend;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import org.apache.tomcat.Jar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BackEndApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BackEndApplication.class);
    }

    public static void main(String[] args) {
        /*new SpringApplicationBuilder()
            .sources(BackEndApplication.class)
            .resourceLoader(new JarResourceLoader())
            .run(args);*/
        SpringApplication.run(BackEndApplication.class, args);
    }
}