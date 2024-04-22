package org.konradchrzanowski.user.service;

import org.konradchrzanowski.utils.common.dto.PasswordResetTokenDTO;
import org.konradchrzanowski.utils.common.payload.request.NewPasswordPutRequest;
import org.konradchrzanowski.utils.common.payload.response.MessageResponse;

public interface PasswordResetService {

    MessageResponse saveNewPassword(PasswordResetTokenDTO passwordResetTokenDTO, NewPasswordPutRequest request);
}
