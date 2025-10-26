package kr.co.nextplayer.base.front.mapper;

import kr.co.nextplayer.base.front.model.Uage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface HomeMapper {

    @Insert("insert visitors_history (ip, reg_date) VALUES (#{ip}, DEFAULT) ")
    int insertVisitorsHistory(String ip);

    @Insert("insert access_info (user_agent, reg_date) VALUES (#{userAgent}, NOW()) ")
    int insertAccessInfo(String userAnge);

    @Select("select count(1) from visitors_history where ip = #{ip} and reg_date >= DATE_ADD(NOW(), INTERVAL -1 HOUR )")
    int selectCountVisitorsIpHistory(String ip);

    @Select("select count(1) from visitors_history")
    int selectCountVisitorsTotal();

    @Select("select count(1) from visitors_history where reg_date >= date_format(now(), '%Y-%m-%d')")
    int selectCountVisitorsToday();

    @Insert("insert visitors_history_minute (ip, reg_date) VALUES (#{ip}, DEFAULT) ")
    int insertVisitorsHistoryMinute(String ip);

    @Select("select count(1) from visitors_history_minute where ip = #{ip} and reg_date >= DATE_ADD(NOW(), INTERVAL -10 MINUTE )")
    int selectCountVisitorsIpHistoryMinute(String ip);

    @Select("select count(1) from visitors_history_minute")
    int selectCountVisitorsTotalMinute();

    @Select("select count(1) from visitors_history_minute where reg_date >= date_format(now(), '%Y-%m-%d')")
    int selectCountVisitorsTodayMinute();

    @Select("select media_id, title, url_link, img_url, media_type, sub_type, source, submit_date from Media where media_type = 'Video' and sub_type != '0' and delete_flag = '0' order by submit_date desc, media_id desc limit 10")
    List<HashMap<String, Object>> selectHomeVideoData();

    @Select("select media_id, title, url_link, img_url, media_type, summary, sub_type, source, DATE_FORMAT(submit_date, '%Y-%m-%d') AS submit_date from Media where media_type = #{mediaType} and delete_flag = '0' order by submit_date desc, media_id desc limit 4")
    List<HashMap<String, Object>> selectHomeMediaData(String mediaType);

    @Insert("insert into push_click (path) values(#{path})")
    void insertPushClick(String path);
}
