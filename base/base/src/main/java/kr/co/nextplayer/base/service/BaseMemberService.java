package kr.co.nextplayer.base.service;

import kr.co.nextplayer.base.model.AuthenticationMember;
import kr.co.nextplayer.base.mapper.BaseMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Base 회원 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BaseMemberService {

    private final BaseMemberMapper memberMapper;

    /**
     * 로그인 회원 정보 조회
     * @param memberCd
     * @return
     */
    public AuthenticationMember getMemberByMemberCd(String memberCd) {
        return memberMapper.getMemberByMemberCd(memberCd);
    }

    
}
