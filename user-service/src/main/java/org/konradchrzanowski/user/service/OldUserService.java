package org.konradchrzanowski.user.service;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.user.entities.UserVO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class OldUserService {
    public UserVO save(UserVO userVO) {

        String userId = String.valueOf(new Date().getTime());
        userVO.setId(userId);
        userVO.setRole("USER");

        return userVO;
    }

}
