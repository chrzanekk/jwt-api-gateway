package org.konradchrzanowski.user.services;

import lombok.AllArgsConstructor;
import org.konradchrzanowski.user.entities.UserVO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {
    public UserVO save(UserVO userVO) {

        String userId = String.valueOf(new Date().getTime());
        userVO.setId(userId);
        userVO.setRole("USER");

        return userVO;
    }

}
