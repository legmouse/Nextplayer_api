package kr.co.nextplayer.back.batch;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BackBatchApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(BackBatchApplication.class)
            .resourceLoader(new JarResourceLoader())
            .run(args);
    }
}
