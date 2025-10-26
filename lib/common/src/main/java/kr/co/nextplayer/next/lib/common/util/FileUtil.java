package kr.co.nextplayer.next.lib.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {

    public static String saveFile(byte[] bytes, String rootFilePath, String fileSuffix) {

        LocalDate localDate = LocalDate.now();

        String directoryPath = String.format("%s/%d/%d/", rootFilePath, localDate.getYear(), localDate.getMonthValue());
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = String.format("%s/%d/%d/%s", rootFilePath, localDate.getYear(), localDate.getMonthValue(), UUID.randomUUID().toString() + "." + fileSuffix);
        File file = new File(filePath);

        try {
            Files.write(Paths.get(file.getPath()), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }

    public static List<Long> getFieldIdsList(String... elements) {
        List<Long> fieldIdsList = new ArrayList<>();
        if (elements == null) {
            return null;
        }

        for (String id : elements) {
            if (StringUtils.isNotEmpty(id)) {
                fieldIdsList.add(Long.valueOf(id));
            }
        }

        return fieldIdsList;
    }
}
