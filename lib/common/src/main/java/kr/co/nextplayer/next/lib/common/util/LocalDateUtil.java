package kr.co.nextplayer.next.lib.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class LocalDateUtil {


    public final static ZoneId ZONE_UTC = ZoneId.of("UTC");
    public final static ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatter_ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String format_6 = "yyyy-MM-dd";
    private static final String format_10 = "yyyy-MM-dd HH:mm";
    private static final String format_12 = "yyyy-MM-dd HH:mm:ss";

    public static String formatKST(LocalDateTime datetime) {
        return formatter.format(ZonedDateTime.of(datetime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST(Date datetime) {
        LocalDateTime localDateTime = DateUtils.asLocalDateTime(datetime);
        return formatter.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST_ymd(LocalDateTime localDateTime) {
        return formatter_ymd.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatKST_format(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(ZonedDateTime.of(localDateTime, ZONE_UTC).withZoneSameInstant(ZONE_KST));
    }

    public static String formatUTC_format(LocalDateTime datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(datetime);
    }

    public static LocalDate getLocalDate() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDate now = LocalDate.now(zoneId);
        return now;
    }

    public static LocalDateTime getLocalDateByZone() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(zoneId);
        return now;
    }

    public static LocalDateTime getLocalDateTimeByZone() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(zoneId);
        return now;
    }

    public static String parseStrTimeToZoneUTC(String timestamp) {

        if (StringUtils.isNotEmpty(timestamp)) {

            String format = null;
            int index = timestamp.indexOf(":");
            if (index < 0) {
                format = format_6;

            } else {
                int lastIndex = timestamp.lastIndexOf(":");
                if (index == lastIndex) {
                    format = format_10;
                } else {
                    format = format_12;
                }
            }

            Date parseDate = DateUtils.parse(timestamp, format);
            LocalDateTime localDateTime = DateUtils.asLocalDateTime(parseDate);
            String formatDate = formatter.format(ZonedDateTime.of(localDateTime, ZONE_KST).withZoneSameInstant(ZONE_UTC));
            return formatDate;
        } else {
            return null;
        }
    }

    public static String getNowDate() {
        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 원하는 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 형식에 맞게 날짜를 문자열로 변환
        return currentDate.format(formatter);
    }

    /*public static void main(String[] args) throws Exception {
        //北京时间
        LocalDateTime dateTime1 = LocalDateTime.now();
        //美国洛杉矶时间
        LocalDateTime dateTime2 = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
        //Seoul时间
        Date dateTime_Seoul = getLocalDateByZone();
        //2020年12月1日 13时50分
        LocalDateTime dateTime3 = LocalDateTime.of(2020, 12, 1, 13, 50);
        //2020年12月2日 8时50分30秒
        LocalDateTime dateTime4 = LocalDateTime.of(2020, 12, 2, 8, 50, 30);
        //2020年12月14日 12时50秒
        LocalDateTime dateTime5 = LocalDateTime.parse("2020-12-14T12:00:50");
        //2020年12月8日 17时30分50秒
        LocalDateTime dateTime6 = LocalDateTime.parse("2020-12-08 17:30:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("北京时间：" + dateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("美国洛杉矶时间：" + dateTime2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Seoul时间：" + dateTime_Seoul);
        System.out.println("指定时间1：" + dateTime3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("指定时间2：" + dateTime4.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("指定时间3：" + dateTime5.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("指定时间4：" + dateTime6.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }*/
}

