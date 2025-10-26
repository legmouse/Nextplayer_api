package kr.co.nextplayer.base.store.mapper;

import kr.co.nextplayer.base.store.model.Store;

import java.util.HashMap;

public interface StoreMapper {

    Store selectMemberCMoney(String memberCd);

}
