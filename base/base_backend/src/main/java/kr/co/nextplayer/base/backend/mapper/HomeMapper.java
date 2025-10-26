package kr.co.nextplayer.base.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface HomeMapper {

    @Insert("insert visitors_history (ip, reg_date) VALUES (#{ip}, DEFAULT) ")
    int insertVisitorsHistory(String ip);

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
}
