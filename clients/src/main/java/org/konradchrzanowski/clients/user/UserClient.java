package org.konradchrzanowski.clients.user;

import org.konradchrzanowski.clients.user.payload.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "USER",
        url = "${clients.user.url}")
public interface UserClient {

    @PostMapping(path = "/users")
    UserVO saveUser(@RequestBody UserVO user);
}
