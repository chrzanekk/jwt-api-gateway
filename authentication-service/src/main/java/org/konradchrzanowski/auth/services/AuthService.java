package org.konradchrzanowski.auth.services;

import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.request.RegisterRequest;

public interface AuthService {


    UserDTO register(RegisterRequest registerRequest);

    String confirmUser(String token);

    UserDTO saveNewPassword(NewPasswordPutRequest newPasswordPutRequest);

}
