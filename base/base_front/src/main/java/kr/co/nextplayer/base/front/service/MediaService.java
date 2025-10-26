package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.cup.dto.cup.CupInfoDto;
import kr.co.nextplayer.base.cup.mapper.CupMapper;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.response.media.*;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.league.dto.LeagueListDto;
import kr.co.nextplayer.base.league.mapper.LeagueMapper;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.media.dto.GameReqDto;
import kr.co.nextplayer.base.media.dto.MediaDetailDto;
import kr.co.nextplayer.base.media.dto.MediaDto;
import kr.co.nextplayer.base.media.dto.MediaReqDto;
import kr.co.nextplayer.base.media.mapper.MediaMapper;
import kr.co.nextplayer.base.media.model.Creator;
import kr.co.nextplayer.base.media.model.Media;
import kr.co.nextplayer.base.team.dto.TeamDto;
import kr.co.nextplayer.base.team.mapper.TeamMapper;
import kr.co.nextplayer.base.team.model.Team;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class MediaService {

    @Resource
    private UageMapper uageMapper;

    @Resource
    private MediaMapper mediaMapper;

    @Resource
    private CupMapper cupMapper;

    @Resource
    private LeagueMapper leagueMapper;

    @Resource
    private TeamMapper teamMapper;

    public MediaResponse getMediaList(MediaReqDto mediaReqDto) {

        String categoryType = "";

        if (mediaReqDto.getMediaType().equals("Video")) {
            categoryType = "M0001";
        } else if (mediaReqDto.getMediaType().equals("News")) {
            categoryType = "M0002";
        } else if (mediaReqDto.getMediaType().equals("Blog")) {
            categoryType = "M0003";
        }

        List<HashMap<String, Object>> menuList = uageMapper.selectCategoryMenu(categoryType);

        int totalCount = mediaMapper.selectMediaListCnt(mediaReqDto);

        PageInfo pageInfo = new PageInfo(mediaReqDto.getCurPage(), mediaReqDto.getPageSize());

        mediaReqDto.setOffset(pageInfo.getOffset());
        mediaReqDto.setPageSize(pageInfo.getPageSize());

        List<Media> mediaList = mediaMapper.selectMediaList(mediaReqDto);
        List<MediaDto> resultList = new ArrayList<MediaDto>();

        List<Creator> creatorList = mediaMapper.selectCreatorList(mediaReqDto);

        if (mediaList.size() > 0) {
            for (Media media: mediaList) {

                List<Media> childList = mediaMapper.selectMediaChildList(media.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }
                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }

                MediaDto mediaDto = MediaDto.builder()
                        .media_id(media.getMediaId())
                        .title(media.getTitle())
                        .content(media.getContent())
                        .source(media.getSource())
                        .summary(media.getSummary())
                        .url_link(media.getUrlLink())
                        .img_url(media.getImgUrl())
                        .media_type(media.getMediaType())
                        .type(media.getType())
                        .submit_date(media.getSubmitDate())
                        .cup_tag(cup_tag)
                        .league_tag(league_tag)
                        .team_tag(team_tag)
                        .video_type((media.getVideoType()))
                        .cup_match(cup_match)
                        .league_match(league_match)
                        .video_type((media.getVideoType()))
                        .creator(media.getCreator())
                        .showFlag(media.getShowFlag())
                        .build();
                resultList.add(mediaDto);
            }
        }

        MediaResponse mediaResponse = MediaResponse.builder()
            .total_count(totalCount)
            .mediaList(resultList)
            .menuList(menuList)
            .creatorList(creatorList)
            .build();

        return mediaResponse;
    }

    public GameMediaResponse getGameMediaList(GameReqDto gameReqDto) {

        String infoTB = gameReqDto.getAgeGroup() + "_" + gameReqDto.getGameType() + "_Info";

        gameReqDto.setInfoTB(infoTB);

        if (gameReqDto.getGameType().equals("Cup")) {
            gameReqDto.setCupSubMatchTB(gameReqDto.getAgeGroup() + "_Cup_Sub_Match");
            gameReqDto.setCupMainMatchTB(gameReqDto.getAgeGroup() + "_Cup_Main_Match");
            gameReqDto.setCupTourMatchTB(gameReqDto.getAgeGroup() + "_Cup_Tour_Match");
        } else if (gameReqDto.getGameType().equals("League")) {
            gameReqDto.setLeagueMatchTB(gameReqDto.getAgeGroup() + "_League_Match");
        }





        int totalCount = mediaMapper.selectGameMediaListCnt(gameReqDto);

        PageInfo pageInfo = new PageInfo(gameReqDto.getCurPage(), gameReqDto.getPageSize());

        gameReqDto.setOffset(pageInfo.getOffset());
        gameReqDto.setPageSize(pageInfo.getPageSize());

        List<Media> mediaList = mediaMapper.selectGameMediaList(gameReqDto);
        List<MediaDto> resultList = new ArrayList<MediaDto>();


        if (mediaList.size() > 0) {
            for (Media media: mediaList) {

                List<Media> childList = mediaMapper.selectMediaChildList(media.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }
                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }

                MediaDto mediaDto = MediaDto.builder()
                    .media_id(media.getMediaId())
                    .title(media.getTitle())
                    .content(media.getContent())
                    .source(media.getSource())
                    .summary(media.getSummary())
                    .url_link(media.getUrlLink())
                    .img_url(media.getImgUrl())
                    .media_type(media.getMediaType())
                    .type(media.getType())
                    .submit_date(media.getSubmitDate())
                    .cup_tag(cup_tag)
                    .league_tag(league_tag)
                    .team_tag(team_tag)
                    .video_type((media.getVideoType()))
                    .cup_match(cup_match)
                    .league_match(league_match)
                    .video_type((media.getVideoType()))
                    .creator(media.getCreator())
                    .showFlag(media.getShowFlag())
                    .build();
                resultList.add(mediaDto);
            }
        }

        GameMediaResponse mediaResponse = GameMediaResponse.builder()
            .total_count(totalCount)
            .mediaList(resultList)
            .build();

        return mediaResponse;
    }

    public VideoMediaResponse getVideoMediaList(MediaReqDto mediaReqDto) {
        List<Media> videoList = mediaMapper.selectVideoMediaList(mediaReqDto);
        List<MediaDto> resultList = new ArrayList<MediaDto>();
        if (videoList.size() > 0) {
            for (Media video : videoList) {

                List<Media> childList = mediaMapper.selectMediaChildList(video.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable() != null) {

                        }

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }

                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }

                MediaDto mediaDto = MediaDto.builder()
                    .media_id(video.getMediaId())
                    .title(video.getTitle())
                    .content(video.getContent())
                    .url_link(video.getUrlLink())
                    .media_type(video.getMediaType())
                    .video_type(video.getVideoType())
                    .cup_tag(cup_tag)
                    .league_tag(league_tag)
                    .team_tag(team_tag)
                    .cup_match(cup_match)
                    .league_match(league_match)
                    .reg_date(video.getRegDate())
                    .showFlag(video.getShowFlag())
                    .build();
                resultList.add(mediaDto);
            }
        }
        VideoMediaResponse videoMediaResponse = VideoMediaResponse.builder()
            .videoMediaList(resultList)
            .build();

        return videoMediaResponse;
    }

    public NewsMediaResponse getNewsMediaList(MediaReqDto mediaReqDto) {
        List<Media> newsList = mediaMapper.selectNewsMediaList(mediaReqDto);
        List<MediaDto> resultList = new ArrayList<MediaDto>();
        if (newsList.size() > 0) {
            for (Media news : newsList) {
                MediaDto mediaDto = MediaDto.builder()
                    .media_id(news.getMediaId())
                    .title(news.getTitle())
                    .source(news.getSource())
                    .summary(news.getSummary())
                    .url_link(news.getUrlLink())
                    .img_url(news.getImgUrl())
                    .submit_date(news.getSubmitDate())
                    .media_type(news.getMediaType())
                    .reg_date(news.getRegDate())
                    .build();
                resultList.add(mediaDto);
            }
        }
        NewsMediaResponse newsMediaResponse = NewsMediaResponse.builder()
            .newsMediaList(resultList)
            .build();

        return newsMediaResponse;
    }

    public BlogMediaResponse getBlogMediaList(MediaReqDto mediaReqDto) {
        List<Media> newsList = mediaMapper.selectBlogMediaList(mediaReqDto);
        List<MediaDto> resultList = new ArrayList<MediaDto>();
        if (newsList.size() > 0) {
            for (Media news : newsList) {
                MediaDto mediaDto = MediaDto.builder()
                    .media_id(news.getMediaId())
                    .title(news.getTitle())
                    .source(news.getSource())
                    .summary(news.getSummary())
                    .url_link(news.getUrlLink())
                    .img_url(news.getImgUrl())
                    .submit_date(news.getSubmitDate())
                    .media_type(news.getMediaType())
                    .reg_date(news.getRegDate())
                    .build();
                resultList.add(mediaDto);
            }
        }
        BlogMediaResponse blogMediaResponse = BlogMediaResponse.builder()
            .blogMediaList(resultList)
            .build();

        return blogMediaResponse;
    }

    public MediaDetailListResponse getMediaDetailList(MediaReqDto mediaReqDto) {

        List<Media> mediaDetailList = mediaMapper.selectMediaDetailList(mediaReqDto);
        List<MediaDetailDto> result = new ArrayList<MediaDetailDto>();

        if (mediaDetailList.size() > 0) {
            for (Media media : mediaDetailList) {

                List<Media> childList = mediaMapper.selectMediaChildList(media.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable() != null) {

                        }

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }

                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }

                MediaDetailDto mediaDetailDto = MediaDetailDto.builder()
                    .media_id(media.getMediaId())
                    .title(media.getTitle())
                    .content(media.getContent())
                    .source(media.getSource())
                    .summary(media.getSummary())
                    .url_link(media.getUrlLink())
                    .img_url(media.getImgUrl())
                    .media_type(media.getMediaType())
                    .video_type(media.getVideoType())
                    .cup_tag(cup_tag)
                    .league_tag(league_tag)
                    .team_tag(team_tag)
                    .cup_match(cup_match)
                    .league_match(league_match)
                    .reg_date(media.getRegDate())
                    .submit_date(media.getSubmitDate())
                    .creator(media.getCreator())
                    .showFlag(media.getShowFlag())
                    .build();

                result.add(mediaDetailDto);
            }
        }


        MediaDetailListResponse mediaResponse = MediaDetailListResponse.builder()
            .mediaList(result)
            .build();

        return mediaResponse;
    }

    public MediaDetailListResponse getMediaTeamDetailList(MediaReqDto mediaReqDto) {

        List<Media> mediaDetailList = mediaMapper.selectMediaDetailListForTeam(mediaReqDto);
        List<MediaDetailDto> result = new ArrayList<MediaDetailDto>();

        if (mediaDetailList.size() > 0) {
            for (Media media : mediaDetailList) {

                List<Media> childList = mediaMapper.selectMediaChildList(media.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable() != null) {

                        }

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }

                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }

                MediaDetailDto mediaDetailDto = MediaDetailDto.builder()
                    .media_id(media.getMediaId())
                    .title(media.getTitle())
                    .content(media.getContent())
                    .source(media.getSource())
                    .summary(media.getSummary())
                    .url_link(media.getUrlLink())
                    .img_url(media.getImgUrl())
                    .media_type(media.getMediaType())
                    .video_type(media.getVideoType())
                    .cup_tag(cup_tag)
                    .league_tag(league_tag)
                    .team_tag(team_tag)
                    .cup_match(cup_match)
                    .league_match(league_match)
                    .reg_date(media.getRegDate())
                    .submit_date(media.getSubmitDate())
                    .creator(media.getCreator())
                    .showFlag(media.getShowFlag())
                    .build();

                result.add(mediaDetailDto);
            }
        }


        MediaDetailListResponse mediaResponse = MediaDetailListResponse.builder()
            .mediaList(result)
            .build();

        return mediaResponse;
    }

    public MediaDetailResponse getMediaDetail(MediaReqDto mediaReqDto) {

        Media mediaDetailInfo = mediaMapper.selectMediaDetailInfo(mediaReqDto);

        /*Long bfViewCnt = mediaDetailInfo.getViewCnt();
        mediaReqDto.setViewCnt(bfViewCnt+1);

        mediaMapper.updateMediaViewCnt(mediaReqDto);*/

        List<Media> childList = mediaMapper.selectMediaChildList(mediaDetailInfo.getMediaId());

        mediaReqDto.setSubType(mediaDetailInfo.getSubType());
        mediaReqDto.setSubmitDate(mediaDetailInfo.getSubmitDate());

        List<Media> type = mediaMapper.selectPrevNextMedia(mediaReqDto);

        Optional<Media> nextMediaOptional = type.stream() .filter(media -> media.getMType().equals("next")) .findFirst();
        Media nextMedia = nextMediaOptional.orElse(null);

        Optional<Media> prevMediaOptional = type.stream() .filter(media -> media.getMType().equals("prev")) .findFirst();
        Media prevMedia = prevMediaOptional.orElse(null);


        List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

        List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

        if (childList.size() > 0) {
            for (Media child : childList) {

                String childTB = "";
                String childId = "";
                String childKey = "";

                if (child.getParentTable().contains("Cup")) {

                    childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                    childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                    if (childTB.contains("Cup_Sub")) {
                        childKey = "a.cup_sub_match_id";
                    } else if (childTB.contains("Cup_Main")) {
                        childKey = "a.cup_main_match_id";
                    } else if (childTB.contains("Cup_Tour")) {
                        childKey = "a.cup_tour_match_id";
                    }

                    Cup cup = Cup.builder()
                        .parentTB(child.getParentTable())
                        .parentId(child.getParentId())
                        .childTB(childTB)
                        .childId(childId)
                        .childKey(childKey)
                        .build();

                    cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                    if (!StrUtil.isEmpty(childTB)) {
                        cup_match = cupMapper.selectCupMatchListForMedia(cup);
                    }
                }

                if (child.getParentTable().contains("League")) {

                    childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                    childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                    childKey = "a.league_match_id";

                    League league = League.builder()
                        .parentTB(child.getParentTable())
                        .parentId(child.getParentId())
                        .childTB(childTB)
                        .childId(childId)
                        .childKey(childKey)
                        .build();

                    league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                    if (!StrUtil.isEmpty(childTB)) {
                        league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                    }
                }

                if (child.getParentTable().contains("Team")) {
                    TeamDto teamDto = TeamDto.builder()
                        .childId("(" + child.getChildId() + ")")
                        .build();
                    team_tag = teamMapper.selectTeamListForMedia(teamDto);
                }

            }
        }

        MediaDto mediaDetail = MediaDto.builder()
            .media_id(mediaDetailInfo.getMediaId())
            .title(mediaDetailInfo.getTitle())
            .content(mediaDetailInfo.getContent())
            .source(mediaDetailInfo.getSource())
            .summary(mediaDetailInfo.getSummary())
            .url_link(mediaDetailInfo.getUrlLink())
            .media_type(mediaDetailInfo.getMediaType())
            .video_type(mediaDetailInfo.getVideoType())
            .img_url(mediaDetailInfo.getImgUrl())
            .ref_url(mediaDetailInfo.getRefUrl())
            .submit_date(mediaDetailInfo.getSubmitDate())
            .cup_tag(cup_tag)
            .league_tag(league_tag)
            .cup_match(cup_match)
            .league_match(league_match)
            .team_tag(team_tag)
            .creator(mediaDetailInfo.getCreator())
            .prevMedia(prevMedia)
            .nextMedia(nextMedia)
            .showFlag(mediaDetailInfo.getShowFlag())
            .sub_type(mediaDetailInfo.getSubType())
            .build();

        //mediaDetailInfo.setC

        MediaDetailResponse mediaDetailResponse = MediaDetailResponse.builder()
            .mediaDetail(mediaDetail)
            .build();

        return mediaDetailResponse;
    }

    public int updateViewCnt(MediaReqDto mediaReqDto) {

        Media mediaDetailInfo = mediaMapper.selectMediaDetailInfo(mediaReqDto);

        Long bfViewCnt = mediaDetailInfo.getViewCnt();
        mediaReqDto.setViewCnt(bfViewCnt+1);

        return mediaMapper.updateMediaViewCnt(mediaReqDto);
    }
}
