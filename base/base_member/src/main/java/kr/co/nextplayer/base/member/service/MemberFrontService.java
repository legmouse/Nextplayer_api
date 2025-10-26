package kr.co.nextplayer.base.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.nextplayer.base.member.dto.*;
import kr.co.nextplayer.base.member.mapper.MemberConfirmMapper;
import kr.co.nextplayer.base.member.mapper.MemberInactiveMapper;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.mapper.MemberSnsMapper;
import kr.co.nextplayer.base.member.model.*;
import kr.co.nextplayer.base.store.mapper.StoreMapper;
import kr.co.nextplayer.base.store.model.Store;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import kr.co.nextplayer.next.lib.common.util.SecretUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@Transactional
public class MemberFrontService {

    @Resource
    private RedisOperation redisOperation;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberInactiveMapper memberInactiveMapper;
    @Resource
    private MemberSnsMapper memberSnsMapper;
    @Resource
    private MemberConfirmMapper memberConfirmMapper;
    @Resource
    private StoreMapper storeMapper;


    private String generateUserId(String phoneNO) throws CommonLogicException {
        String generateUserId = "";

        try {
            String redisKey = InetAddress.getLocalHost().getHostName() + phoneNO;
            boolean hasKey = redisOperation.hasKey(redisKey);
            if (hasKey) {
                throw new CommonLogicException(ApiState.ACCESS_TOO_FREQUENTLY.getCode(), ApiState.ACCESS_TOO_FREQUENTLY.getMessage());
            }

            //넥스트플레이어 11
            String memberCodePriPix = "11";
            generateUserId = memberCodePriPix + "U" + System.currentTimeMillis() + redisOperation.getIncrement();
            redisOperation.set(redisKey, generateUserId, 3);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CommonLogicException(ApiState.SYSTEM.getCode(), "error.900");
        }

        return generateUserId;
    }

    /**
     * 회원 인증정보 수정
     */
    public Boolean modifyMemberConfirm(MemberConfirmRegDto dto) throws CommonLogicException {

        if (dto != null) {
            MemberConfirm memberConfirm = MemberConfirm.builder()
                    .memberCd(dto.getMemberCd())
                    .build();
            memberConfirm.setBirthDate(dto.getBirthDate());
            memberConfirm.setSex(dto.getSex());
            memberConfirm.setMemberName(dto.getMemberName());
            memberConfirm.setPhoneNo(dto.getPhoneNo());
            memberConfirm.setAgeGroup(dto.getAgeGroup());
            memberConfirm.setEmail(dto.getEmail());
            memberConfirm.setAge(dto.getAge());
            memberConfirmMapper.updateSelective(memberConfirm);
            log.info("휴대폰 본인 인증정보 수정");

        }

        return true;
    }


    /**
     * 회원 수정 이메일 중복체크
     */
    public int emailUseCheckForModify(String memberId, String memberCd) {
        int activeEmailCount = memberMapper.checkActiveEmailForModify(memberId.trim(), memberCd.trim());
        if (activeEmailCount > 0) {
            return activeEmailCount;
        }
        return 0;
    }


    /**
     * 회원 삭제
     * @param memberCd
     * @return
     */
    public int deleteMember(String memberCd) {
        int memberDelete = memberMapper.deleteMember(memberCd);
        int memberConfirmDelete = memberConfirmMapper.deleteMemberConfirm(memberCd);
        memberConfirmMapper.deleteMemberSns(memberCd);

        if (memberDelete > 0 && memberConfirmDelete > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 개인회원 수정
     */
    @Transactional(rollbackFor = Exception.class)
    public int modifyMember(MemberModifyDto dto, ServerHttpRequest request) throws CommonLogicException, JsonProcessingException {

        Member memberSave = Member.builder()
            .regDate(LocalDateUtil.getLocalDateByZone())
            //회원 상태(1:일반)
            .memberCd(dto.getMemberCd())
            .build();
        BeanUtils.copyProperties(dto, memberSave);

        MemberConfirmRegDto confirm = dto.getMemberConfirm();
        confirm.setMemberCd(dto.getMemberCd());

        if (confirm != null) {
            modifyMemberConfirm(confirm);
        }

        int i = memberMapper.updateSelective(memberSave);

        return i;
    }

    public MemberInfoResDto memberInfo(MemberInfoDto dto) throws CommonLogicException, JsonProcessingException {

        MemberInfoResDto memberInfoResDto = MemberInfoResDto.builder().build();

        MemberInfo memberInfo = memberMapper.selectMemberInfo(dto.getMemberCd());
        if (memberInfo != null) {
            memberInfoResDto.setMember_cd(memberInfo.getMemberCd());
            memberInfoResDto.setMember_id(memberInfo.getMemberId());
            memberInfoResDto.setMember_nickname(memberInfo.getMemberNickname());
            memberInfoResDto.setEmail(memberInfo.getEmail());
            memberInfoResDto.setPhone_no(memberInfo.getPhoneNo());
            memberInfoResDto.setSex(memberInfo.getSex());
            memberInfoResDto.setAge(memberInfo.getAge());
            memberInfoResDto.setMember_name(memberInfo.getMemberName());
            memberInfoResDto.setAge_group(memberInfo.getAgeGroup());
            memberInfoResDto.setBirth_date(memberInfo.getBirthDate());
            memberInfoResDto.setChildren_age(memberInfo.getChildrenAge());
            memberInfoResDto.setCert_type(memberInfo.getCertType());
            memberInfoResDto.setMember_type(memberInfo.getMemberType());
            memberInfoResDto.setInterest_age(memberInfo.getInterestAge());
            memberInfoResDto.setInterest_team(memberInfo.getInterestTeam());
            memberInfoResDto.setTeamInfo(memberInfo.getTeamInfo());

            Store cMoney = storeMapper.selectMemberCMoney(memberInfo.getMemberCd());

            if (cMoney != null) {
                memberInfoResDto.setCMoney(cMoney.getCMoney());
            }

        }

        return memberInfoResDto;
    }


    /**
     * 비밀번호 재 확인
     */
    public Boolean checkPassWord(String memberCd, String passWord) throws CommonLogicException {

        //회원 정보
        log.error("selectByMemberCd start");
        Member member = memberMapper.selectByMemberCd(memberCd);
        log.error("selectByMemberCd end");
        //입력한 비번 암호화
        String secretPw = SecretUtil.generateUserPassWord(passWord);
        log.error("generateUserPassWord end");
        //비번 확인.
        if (!member.getMemberPwd().equals(secretPw)) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg61_auth_idAndPwIsNotMatch");
        }

        return true;

    }

    /**
     * 관심 연령, 관심 팀 수정
     */
    @Transactional(rollbackFor = Exception.class)
    public int modifyMemberInterest(MemberInterestReqDto dto, ServerHttpRequest request) throws CommonLogicException, JsonProcessingException {

        int i = memberMapper.updateMemberInterest(dto);

        return i;
    }

    /**
     * 푸시 세팅 현황
     */
    public MemberPushSettingDto selectMemberPush(String memberCd) {
        return memberMapper.selectMemberPush(memberCd);
    }

    /**
     * 푸시 세팅
     */
    public void updateMemberPushSetting(MemberPushSettingDto memberPushSettingDto) {
        memberMapper.updateMemberPushSetting(memberPushSettingDto);
    }

    public List<MemberPushHistoryDto> selectPushHistory(String memberCd){
        return memberMapper.selectPushHistory(memberCd);
    }

    public void updatePushRead(int pushId) {
        memberMapper.updatePushRead(pushId);
    }

    public boolean selectPushBellCheck(String memberCd) {
        return memberMapper.selectPushBellCheck(memberCd);
    }

    public void updatePushReadAll(String memberCd) {
        memberMapper.updatePushReadAll(memberCd);
    }

}
