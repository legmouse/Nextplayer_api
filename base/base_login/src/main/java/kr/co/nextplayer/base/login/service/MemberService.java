package kr.co.nextplayer.base.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.nextplayer.base.login.dto.*;
import kr.co.nextplayer.base.login.oauth.utils.StringUtils;
import kr.co.nextplayer.base.member.dto.MemberConfirmRegDto;
import kr.co.nextplayer.base.member.dto.MemberSnsRegDto;
import kr.co.nextplayer.base.member.mapper.MemberConfirmMapper;
import kr.co.nextplayer.base.member.mapper.MemberInactiveMapper;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.mapper.MemberSnsMapper;
import kr.co.nextplayer.base.member.model.*;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.co.nextplayer.next.lib.common.constants.SystemSettingCode.*;

@Slf4j
@Service
@Transactional
public class MemberService extends CommService {

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
     * 개인회원 가입
     */
    @Transactional(rollbackFor = Exception.class)
    public int registerMember(MemberRegisterDto dto, ServerHttpRequest request) throws CommonLogicException, JsonProcessingException {

        Member memberSave = Member.builder()
            .regDate(LocalDateUtil.getLocalDateByZone())
            //회원 상태(1:일반)
            .memberState(MEMBER_STATE_GENERAL.getCode())
            .lastConnectIp(WebFluxIPUtil.getClientIp(request))
            .lastConnectDate(LocalDateUtil.getLocalDateByZone())
            .pwdUpdDate(LocalDateUtil.getLocalDateByZone())
            .build();
        BeanUtils.copyProperties(dto, memberSave);

        if(!StringUtils.isEmpty(dto.getMemberPwd())){
            memberSave.setMemberPwd(SecretUtil.generateUserPassWord(dto.getMemberPwd()));
        }

        MemberConfirmRegDto confirm = dto.getMemberConfirm();
        // generate a member cd
        if (confirm != null) {
            String generateUserId = generateUserId(dto.getMemberConfirm().getPhoneNo());
            log.info("generateUserId : {}", generateUserId);
            memberSave.setMemberCd(generateUserId);
        }

        //인증정보
        if (confirm != null) {
            addMemberConfirm(confirm, dto.getMemberSns(), memberSave.getMemberCd(), request);
        }


        int i = memberMapper.insertSelective(memberSave);

        //회원 sns정보 연동
        if (i > 0) {
            //sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
            addMemberSns(dto, memberSave.getMemberCd());
        }

        return i;
    }

    /**
     * 회원 인증정보
     */
    public Boolean addMemberConfirm(MemberConfirmRegDto dto, MemberSnsRegDto snsDto, String memberCd, ServerHttpRequest request) throws CommonLogicException {

        if (dto != null) {

            //이메일 중복체크
            if (dto.getEmail() != null) {
                int confirmCount = checkConfirmEmail(dto.getEmail());
                if (confirmCount > 0) {
                    throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg92_member_emailUsed");
                }
            }

            //전화,이름,생년일,성별 중복체크
            if (dto.getPhoneNo() != null) {
                int confirmCount = checkMemberConfirmMobile(dto.getPhoneNo(), dto.getMemberName(),
                    String.valueOf(dto.getBirthDate()), dto.getSex());
                if (confirmCount > 0) {
                    throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg95_member_mobileUsed");
                }
            }

            //개인일 경우
            String certType = dto.getCertType();

            //인증수단(휴대폰:SMS) 일 경우
            //관리자 페이지에서 회원 등록할 경우
            if (CERT_TYPE_SMS.getCode().equals(certType) || CERT_TYPE_A.getCode().equals(certType)) {
                MemberConfirm memberConfirm = getNewMemberConfirm(dto, memberCd);
                memberConfirm.setCertType(certType);
                memberConfirm.setBirthDate(dto.getBirthDate());
                memberConfirm.setSex(dto.getSex());
                memberConfirm.setPhoneNo(dto.getPhoneNo());
                memberConfirm.setAgeGroup(dto.getAgeGroup());
                memberConfirm.setEmail(dto.getEmail());
                memberConfirm.setAge(dto.getAge());
                memberConfirmMapper.insertSelective(memberConfirm);
                log.info("휴대폰 본인 인증정보 저장");
            }

            //인증수단(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE) 일 경우
            if (
                (CERT_TYPE_NAVER.getCode().equals(certType) ||
                    CERT_TYPE_KAKAO.getCode().equals(certType) ||
                    CERT_TYPE_GOOGLE.getCode().equals(certType) ||
                    CERT_TYPE_APPLE.getCode().equals(certType)
                ) && snsDto.getSnsId() != null
            ) {
                MemberConfirm memberConfirm = getNewMemberConfirm(dto, memberCd);
                memberConfirm.setCertType(certType);
                memberConfirm.setEmail(dto.getEmail());
                memberConfirm.setPhoneNo(dto.getPhoneNo());
                memberConfirm.setSex(dto.getSex());
                memberConfirm.setBirthDate(dto.getBirthDate());
                memberConfirm.setAgeGroup(dto.getAgeGroup());
                memberConfirm.setAge(dto.getAge());
                memberConfirmMapper.insertSelective(memberConfirm);
                log.info("sns 인증정보 저장");
            }

        }

        return true;
    }

    /**
     * 신규 인증정보 만들기
     */
    public MemberConfirm getNewMemberConfirm(MemberConfirmRegDto dto, String memberCd) {
        MemberConfirm memberConfirm = MemberConfirm.builder()
            .memberCd(memberCd)
            .memberName(dto.getMemberName())
            .certDate(LocalDateUtil.getLocalDateByZone())
            .build();
        return memberConfirm;
    }

    /**
     * 회원 sns 정보
     */
    public String addMemberSns(MemberRegisterDto dto, String memberCd) {

        if (dto.getMemberSns() != null) {
            MemberSnsRegDto snsRegDto = dto.getMemberSns();
            MemberSns memberSns = MemberSns.builder().build();
            BeanUtils.copyProperties(snsRegDto, memberSns);
            memberSns.setMemberCd(memberCd);
            memberSns.setRegDate(LocalDateUtil.getLocalDateByZone());
            memberSnsMapper.insertSelective(memberSns);

            //sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
            return memberSns.getSnsType();
        } else {
            return null;
        }
    }

    /**
     * 회원 아이디 중복체크
     */
    public int idUseCheck(String memberId) {
        int activeIdCount = memberMapper.checkActiveId(memberId.trim());
        if (activeIdCount > 0) {
            return activeIdCount;
        }
        int inactiveIdCount = memberMapper.checkInactiveId(memberId.trim());
        if (inactiveIdCount > 0) {
            return inactiveIdCount;
        }
        return 0;
    }

    /**
     * 회원 이메일 중복체크
     */
    public int emailUseCheck(String memberId) {
        int activeEmailCount = memberMapper.checkActiveEmail(memberId.trim());
        if (activeEmailCount > 0) {
            return activeEmailCount;
        }
        return 0;
    }

    /**
     * 회원 전화번호 중복체크
     */
    public int phoneNoUseCheck(String phoneNo) {
        int activePhoneNoCount = memberMapper.checkActivePhoneNo(phoneNo.trim());
        if (activePhoneNoCount > 0) {
            return activePhoneNoCount;
        }
        return 0;
    }

    /**
     * 회원인증 이메일 중복체크
     */
    public int checkConfirmEmail(String email) throws CommonLogicException {
        MemberConfirm memberConfirm = memberConfirmMapper.checkEmailExist(email);
        //기존 가입자 있음
        if (memberConfirm != null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 회원인증 닉네임 중복체크
     */
    public int checkActiveNickname(String nickname) {
        int count = memberMapper.checkActiveNickname(nickname);
        if (count > 0) {
            return count;
        }
        return 0;
    }

    /**
     * 사용가능한 sms (전화번호+이름+성별+생년월일) 인지 체크
     */
    public int checkMemberConfirmMobile(String phoneNo, String memberName, String birthDate, String sex) {

        List<MemberConfirm> memberConfirms = getMemberConfirmMobile(phoneNo,memberName,birthDate,sex);

        //기존 가입자 있음
        if (memberConfirms != null && memberConfirms.size() > 0) {
            return memberConfirms.size();
        } else {
            return 0;
        }
    }

    /**
     * sms Confirm
     */
    public List<MemberConfirm> getMemberConfirmMobile(String phoneNo, String memberName, String birthDate, String sex) {

        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("phoneNo", phoneNo);
        searchParam.put("memberName", memberName);
        searchParam.put("birthDate", birthDate);
        searchParam.put("sex", sex);

        List<MemberConfirm> memberConfirms = memberConfirmMapper.selectBySearch(searchParam);

        return memberConfirms;
    }

}
