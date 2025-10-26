package kr.co.nextplayer.base.batch.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*
    3depth
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayDataDto {

    private String matchId;

    private String homeTeamName;
    private String awayTeamName;

    private String matchOrder; //경기번호
    private int audience; // 관중수
    private String weather; //날씨
    private int playTime; //총 경기시간
    private String mainReferee; //주심
    private String subReferee; //부심
    private String standByReferee; //대기심
    private String refereeEvaluation; //심판평가관
    private String gameSupervisor; // 경기감독관
    private MatchStatus status;//경기 상태
    private String scoreType;
    private int homeScore;
    private int awayScore;
    private int homePenaltyScore;
    private int awayPenaltyScore;

    private List<PlayDataDto> homePlaySelectionData = new ArrayList<>();
    private List<PlayDataDto> awayPlaySelectionData = new ArrayList<>();
    private List<PlayDataDto> homePlayCandidateData = new ArrayList<>();
    private List<PlayDataDto> awayPlayCandidateData = new ArrayList<>();
    private List<ChangeDataDto> homeChangeData = new ArrayList<>();
    private List<ChangeDataDto> awayChangeData = new ArrayList<>();

    private List<StaffDataDto> homeStaffData = new ArrayList<>();
    private List<StaffDataDto> awayStaffData = new ArrayList<>();
    private List<StaffPenaltyDataDto> homeStaffPenaltyData = new ArrayList<>();
    private List<StaffPenaltyDataDto> awayStaffPenaltyData = new ArrayList<>();
    private List<OwnGoalDataDto> homeOwnGoalData = new ArrayList<>();
    private List<OwnGoalDataDto> awayOwnGoalData = new ArrayList<>();

    public enum MatchStatus {
        READY
        , LINEUP
        , START
        , END
    }


}
