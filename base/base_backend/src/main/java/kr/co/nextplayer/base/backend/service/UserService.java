package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.UserDto;
import kr.co.nextplayer.base.backend.mapper.UserMapper;
import kr.co.nextplayer.base.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User login(UserDto userDto) {
        return userMapper.login(userDto);
    }

    @Transactional
    public void keepLogin(String userId, String sessionId, Date next) {
        userMapper.updateKeepLogin(userId, sessionId, next);
    }

    public User checkUserWithSessionKey(String sessionId) {
        System.out.println("sessionId = " + sessionId);
        return userMapper.checkUserWithSessionKey(sessionId);
    }

}
