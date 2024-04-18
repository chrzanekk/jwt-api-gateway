package org.konradchrzanowski.utils.common.payload.response;


import org.konradchrzanowski.utils.common.enumeration.ERole;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        List<ERole> roles
) {}
