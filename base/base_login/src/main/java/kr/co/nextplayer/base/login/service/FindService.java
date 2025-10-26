package kr.co.nextplayer.base.login.service;

import kr.co.nextplayer.base.login.dto.FindCertificationDto;
import kr.co.nextplayer.base.login.dto.FindMemberDto;
import kr.co.nextplayer.base.member.mapper.MemberConfirmMapper;
import kr.co.nextplayer.base.member.mapper.MemberInactiveMapper;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.model.Member;
import kr.co.nextplayer.base.member.model.MemberConfirm;
import kr.co.nextplayer.base.member.model.MemberInactive;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.util.DataEncryptUtil;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import kr.co.nextplayer.next.lib.common.util.SecretUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class FindService {

    @Resource
    private RedisOperation redisOperation;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberInactiveMapper memberInactiveMapper;
    @Resource
    private MemberConfirmMapper memberConfirmMapper;
    @Resource
    private MessageService messageService;


    /**
     * 비밀번호 & 아이디 찾기
     */
    public FindCertificationDto findConfirm(FindMemberDto dto) throws CommonLogicException {

        if (dto != null && dto.getType() != null) {

            String type = dto.getType();

            if ("S".equals(type) || "E".equals(type)) {
                return Certification_phoneNoOrEmail(dto);
            }else {
                throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg1_common_paramErr");
            }

        } else {
            throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg1_common_paramErr");
        }

    }


    //찾기 인증 - 개인(phoneNoOrEmail)
    public FindCertificationDto Certification_phoneNoOrEmail(FindMemberDto dto) throws CommonLogicException {

        //기존 가입자 체크
        Map<String, Object> searchParam = new HashMap<>();
        //인증구분(본인:1,보호자:2,법인:3)
        searchParam.put("confirmType", "1");

        String type = dto.getType();
        String verificationCode = dto.getConfirmCode();
        String phoneNoOrEmail = null;
        if ("S".equals(type)) {
            phoneNoOrEmail = dto.getPhoneNo();
            searchParam.put("phoneNo", phoneNoOrEmail);
        }
        if ("E".equals(type)) {
            phoneNoOrEmail = dto.getEmail();
            searchParam.put("email", phoneNoOrEmail);
        }

        if (phoneNoOrEmail != null && verificationCode != null) {
            Boolean result = messageService.certification_check_code(phoneNoOrEmail, verificationCode);
            if (result) {
                return getMember(searchParam, dto);
            } else {
                return FindCertificationDto.builder().checkState(-1).userCount(0).build();
            }

        } else {
            throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg1_common_paramErr");
        }
    }

    /**
     * 회원 본인 확인
     */
    public FindCertificationDto getMember(Map<String, Object> searchParam, FindMemberDto findMemberDto) throws CommonLogicException {

        FindCertificationDto certificationDto = FindCertificationDto.builder()
            .checkState(1)
            .userCount(0)
            .build();

        List<MemberConfirm> memberConfirms = memberConfirmMapper.selectBySearch(searchParam);

        if (memberConfirms.size() > 0) {

            MemberConfirm memberConfirm = memberConfirms.get(0);
            if (memberConfirm != null) {
                String memberCd = memberConfirm.getMemberCd();
                Member member = getAllMember(memberCd);
                if (member != null) {

                    String memberId = member.getMemberId();

                    member.setMemberName(memberConfirm.getMemberName());
                    // 입력한 정보와 모든 정보가 일치
                    if (checkParam(member, findMemberDto)) {
                        certificationDto.setRegType(memberConfirm.getCertType());
                        certificationDto.setCheckState(1);
                        certificationDto.setUserCount(1);
                        certificationDto.setMemberState(member.getMemberState());
                        certificationDto.setMemberId(DataEncryptUtil.hideEndInfo(3, memberId));
                        certificationDto.setMemberId_noMasking(memberId);
                        certificationDto.setMemberCd(memberCd);
                        certificationDto.setCertDate(memberConfirm.getCertDate());
                    } else {
                        throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg76_adminLogin_findPassParamWrong");
                    }

                }
            }
        }

        return certificationDto;

    }

    //입력정보와 db정보 비교
    public Boolean checkParam(Member member, FindMemberDto findMemberDto) {

//        if (findMemberDto.getName() != null) {
//            String name = member.getMemberName();
//            if (!findMemberDto.getName().equals(name)) {
//                return false;
//            }
//        }

        if (findMemberDto.getId() != null) {
            if (!findMemberDto.getId().equals(member.getMemberId())) {
                return false;
            }
        }

        return true;
    }

    //회원정보 조회(휴면회원 포함)
    public Member getAllMember(String memberCd) {
        Member member = memberMapper.selectByMemberCd(memberCd);
        return member;
    }


    /**
     * 비밀번호 재설정하기
     */
    public void resetPw(String memberId, String memberCd, @RequestParam String newPass) throws CommonLogicException {
        Member member = memberMapper.selectByMemberCd(memberCd);
        if (member == null) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg27_common_dataNotExist");
        }

        if (member.getMemberId().equals(memberId)) {

            Member up_member = Member.builder().build();

            up_member.setMemberCd(memberCd);
            up_member.setMemberPwd(SecretUtil.generateUserPassWord(newPass));

            memberMapper.updateByPrimaryKeySelective(up_member);

        } else {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg27_common_dataNotExist");
        }
    }


}
