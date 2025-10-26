package kr.co.nextplayer.base.mapper;

import kr.co.nextplayer.base.model.AuthenticationMember;

public interface BaseMemberMapper {

    AuthenticationMember getMemberByMemberCd(String memberCd);

    
}
