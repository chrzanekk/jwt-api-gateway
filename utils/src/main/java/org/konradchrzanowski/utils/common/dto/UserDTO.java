package org.konradchrzanowski.utils.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean enabled;
    private Set<RoleDTO> roles;
}
