package org.konradchrzanowski.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOV {
    private String id;
    private String email;
    private String password;
    private String role;
}
