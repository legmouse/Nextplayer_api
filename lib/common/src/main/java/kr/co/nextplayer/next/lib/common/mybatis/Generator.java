package kr.co.nextplayer.next.lib.common.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MBG 코드
 */
public class Generator {
    public static void main(String[] args) throws Exception {
        //MBG 실행 중 경고 메시지
        List<String> warnings = new ArrayList<String>();
        //중복 생성될시 overwrite
        boolean overwrite = true;
        //MBG 설정파일 로드
        InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // MBG 생성
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        //실행 코드 생성
        myBatisGenerator.generate(null);
        //경고 메시지 출력하기
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
