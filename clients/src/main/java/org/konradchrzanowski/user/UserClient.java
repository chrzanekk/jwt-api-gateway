package org.konradchrzanowski.user;

import org.konradchrzanowski.user.payload.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "USER-SERVICE",
        url = "${clients.user-service.url}")
public interface UserClient {

    @PostMapping(path = "/users")
    UserVO saveUser(@RequestBody UserVO user);
}
